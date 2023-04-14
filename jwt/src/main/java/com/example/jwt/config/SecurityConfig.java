package com.example.jwt.config;

import com.example.jwt.config.filter.MyFilter1;
import com.example.jwt.config.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new MyFilter1(),BasicAuthenticationFilter.class);      //BasicAuthenticationFilter 필터보다 Myfilter1 필터가 먼저 실행되게 실행
//        http.addFilterBefore(new MyFilter2(),BasicAuthenticationFilter.class);        //Ek
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)    //세션을 사용하지 않음
                .and()
                .addFilter(corsFilter)      //@CrossOrigin(인증x) , 시큐리티 필터에 등록 (인증O)
                .formLogin().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))   //formlogin 방식 대신 jwt 토큰활용하여 필터링 AuthenticationManager 가 필요함 _WebSecurityConfigurerAdapter가 가지고있음
                .httpBasic().disable()      //http 기본 인증방식을 사용하지 않음
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
    }
}
