package com.climeet.climeet_backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // TODO: 2024/01/02 JWT 토큰 발급 과정 구체화 -> hash
    String jwtSchemeName = "JWT TOKEN";
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(apiInfo())
                .addSecurityItem(securityRequirement())
                .components(components());
    }

    private Info apiInfo() {
        return new Info()
                .title("Climeet Springdoc 테스트")
                .description("Springdoc을 사용한 Climeet Swagger UI 테스트")
                .version("1.0.0")
                .contact(new Contact().name("gmail").url("https://www.google.com/intl/ko/gmail/about/"))
                .description("잘못된 부분이나 오류 발생 시 해당 메일로 문의해주세요.\n\"x-aaaalvgcy2mugvl3nbio4uiweq@w1702553627-cxp994679.slack.com\"");
        // TODO: 2024/01/02  슬랙 웹훅 url로 변경 예정
    }

    private Components components(){
        return new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
                        .scheme("bearer")
                        .bearerFormat("JWT"));
    }

    private SecurityRequirement securityRequirement(){
        return new SecurityRequirement().addList(jwtSchemeName);
    }
}