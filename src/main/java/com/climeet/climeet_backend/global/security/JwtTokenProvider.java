package com.climeet.climeet_backend.global.security;


import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.ClimberRepository;
import com.climeet.climeet_backend.global.response.code.BaseErrorCode;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application-dev.yml")
public class JwtTokenProvider {
    @Value("${jwt.access-token.expire-length}")
    private long accessTokenValidityInMilliseconds;
    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenValidityInMillseconds;
    @Value("${jwt.custom.secretKey}")
    private String SECRET_KEY;

    private SecretKey cachedSecretKey;
    private ClimberRepository climberRepository;

    private SecretKey _getSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }


    // 시크릿 키를 반환하는 method
    public SecretKey getSecretKey() {
        if (cachedSecretKey == null) cachedSecretKey = _getSecretKey();

        return cachedSecretKey;
    }
  public String createAccessToken(String payload){
      return createToken(payload, accessTokenValidityInMilliseconds);
  }

    public String createRefreshToken(Long climberId) {
        return createToken(climberId.toString(), refreshTokenValidityInMillseconds);
    }

    public String refreshAccessToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            Long climberId = Long.parseLong(getPayload(refreshToken));
            Climber climber = climberRepository.findById(climberId).orElseThrow(() -> new GeneralException(ErrorStatus._INVALID_MEMBER));
            return createAccessToken(climber.getPayload());
        } else {
            throw new GeneralException(ErrorStatus._INVALID_JWT);
        }
    }




    public String createToken(String payload, long expireLength){
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, getSecretKey())
            .compact();
    }

    public String getPayload(String token){
      try{
          return Jwts.parser()
              .setSigningKey(SECRET_KEY)
              .parseClaimsJwt(token)
              .getBody()
              .getSubject();
      }catch (ExpiredJwtException e){
          return e.getClaims().getSubject();
      }catch(JwtException e){
          throw new GeneralException(ErrorStatus._INVALID_JWT);
      }
    }
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            if (claimsJws.getBody().getExpiration().before(new Date())) {
                throw new GeneralException(ErrorStatus._EXPIRED_JWT);
            }
            return true;
        } catch (ExpiredJwtException e) {
            throw new GeneralException(ErrorStatus._EXPIRED_JWT);
        } catch (JwtException | IllegalArgumentException exception) {
            throw new GeneralException(ErrorStatus._INVALID_JWT);
        }
    }




}
