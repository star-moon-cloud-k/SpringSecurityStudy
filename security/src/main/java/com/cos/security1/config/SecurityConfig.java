package com.cos.security1.config;

import com.cos.security1.config.Oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity      //스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)      //secured 어노데이션 활성화 , preAuthorize/postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    // 1. 코드 받기(인증이 됨)
    // 2. 멕세스 토큰 받기(권한 생김)
    // 3.사용자 프로필 정보 가져옴
    // 4. 그 정보를 토대로 회원가입
    // 4-2. 정보가 부족함 _ 추가정보 필요(집 주소 , 고객 등급 같은 정보들)
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();          //csrf?
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")      // /login 주소가 호출되면 시큐리티가 낚아채서 로그인을 진행해줌
                .defaultSuccessUrl("/")            //로그인이 성공한 경우 보내지는 링크
                .and()
                .oauth2Login()
                .loginPage("/loginForm")       //구글 로그인이 완료된 후 처리 필요.
                .userInfoEndpoint()            //구글 로그인 -> 엑세스 토큰 + 사용자 정보 프로필 한번에 받음
                .userService(principalOauth2UserService)        //후처리

        ;

    }

}
