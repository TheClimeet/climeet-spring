package com.climeet.climeet_backend.global.security;

import com.climeet.climeet_backend.domain.climber.ClimberService;
import com.climeet.climeet_backend.domain.climber.JwtTokenProvider;
import com.climeet.climeet_backend.domain.user.UserRepository;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public WebConfig(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentUserArgumentResolver(jwtTokenProvider, userRepository));
    }
}