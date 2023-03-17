package io.security.basicsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity      //web security 관련 기능 import 해주는 어노테이션
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated();      //요청에 대한 보안 검사 : 모든 요청에 보안 검사
        http
                .formLogin()
//                .loginPage("/loginPage")      //form login page 직접 설정
                .defaultSuccessUrl("/")        //로그인에 성공한 경우 url
                .failureUrl("/login")   //실패한 경우
                .usernameParameter("userId")        //아이디 변수 이름 변경
                .passwordParameter("passwd")        //비밀번호 변수 이름 변경
                .loginProcessingUrl("/login_proc")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override       //인증에 성공했을 때 자동적으로 인증된 값까지 전달함
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        System.out.println("authentication" + authentication.getName());
                        httpServletResponse.sendRedirect("/");
                    }
                })          //로그인에 성공했을 떄 호출. authenticationSuccess
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        System.out.println("exception" + e.getMessage());
                        httpServletResponse.sendRedirect("/login");
                    }
                })
                .permitAll();   //로그인을 해야하므로 모든 접근자는 접근을 허용한다.

    }
}
