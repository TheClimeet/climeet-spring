package com.climeet.climeet_backend.global.security;

import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null
            && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String authorizationHeader = webRequest.getHeader("authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // "Bearer " 이후 문자열
            return loadUserFromToken(token);
        }
        throw new GeneralException(ErrorStatus._EMPTY_JWT);
    }

    public User loadUserFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtTokenProvider.getSecretKey())
                .parseClaimsJws(token).getBody();

            String targetIndex = "+";

            String targetSubject = claims.getSubject();

            int index = targetSubject.indexOf(targetIndex);
            String userId = targetSubject.substring(0, index);

            return userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_USER));
        } catch (Exception ex) {
            throw new GeneralException(ErrorStatus._INVALID_JWT);
        }
    }
}