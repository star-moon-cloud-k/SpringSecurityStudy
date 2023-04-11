package com.example.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    /**
     * cors가 걸린 모든 요청을 다 허용하게 만듬
     * @CrossOrigin(인증X) , 시큐리티 필터에 등록 인증
     * @return
     */
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);       //내 서버가 응답을 할 떄 json을 자바 스크립트에서 처리할 수 있게 할지를 설정
        config.addAllowedOrigin("*");   //모든 ip에 응답을 허용
        config.addAllowedHeader("*");   //모든 header에 응답을 허용
        config.addAllowedMethod("*");   //모든 post,get,patch 등 허용
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
