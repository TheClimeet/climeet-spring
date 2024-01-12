package com.climeet.climeet_backend.domain.climber;

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
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
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

  public String createRefreshToken(){
      byte[] array = new byte[7];
      new Random().nextBytes(array);
      String generatedString = new String(array, StandardCharsets.UTF_8);
      return createToken(generatedString, refreshTokenValidityInMillseconds);
  }

    public String createToken(String payload, long expireLength){
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS512, getSecretKey())
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
          throw new RuntimeException("유효하지 않은 토큰입니다.");
      }
    }
    public boolean validateToken(String token){
      try{
          Jws<Claims> claimsJws = Jwts.parser()
              .setSigningKey(SECRET_KEY)
              .parseClaimsJws(token);
          return !claimsJws.getBody().getExpiration().before(new Date());
      }catch (JwtException | IllegalArgumentException exception){
          return false;
      }
    }


}
