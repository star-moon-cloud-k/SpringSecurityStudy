package com.example.jwt.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 존재
 *  /login 요청해서 username, password 전송하면 (post)
 *  UsernamePasswordAuthenticationFilter 동작을 함
 *  ID, PW 인증
 */

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    /**
     * 1. username , password 받아서 정상인지 authenticationManager를 사용하여 로그인 시도
     * 2. PrincipalDetailsService 가 호출됨
     * 3. loadUserByUsername 자동으로 실행됨
     * 4. PrincipalDetails를 세션에 담고 JWT 토큰을 만들어서 응답해줌 ( 세션은 권한관리를 위해서 사용)
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도");
        return super.attemptAuthentication(request, response);
    }
}
