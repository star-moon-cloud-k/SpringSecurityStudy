package com.example.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;


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
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도");

        //1.username, password 받아서
        try {
//            BufferedReader br = request.getReader();

//            String input = null;
//             while((input = br.readLine())!= null){
//                 System.out.println(input);
//             }
            ObjectMapper ob = new ObjectMapper();
            User user = ob.readValue(request.getInputStream(), User.class);
//            System.out.println(request.getInputStream().toString());
            System.out.println(user);
            //토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            //PrincipalDetailsService 의 loadUserByUsername()함수 실행
            Authentication authentication = authenticationManager.authenticate(authenticationToken); //로그인 한 정보가 담김

            //authenticaiton 객체가 session 영역에 저장됨
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());
            System.out.println("-----------로그인-------------");
            return authentication;
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
//        return super.attemptAuthentication(request, response);

    }

    /**
     * attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthenticaiton 함수가 실행됨
     * JTW 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해줌
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증이 완료됨");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        //RSA x -> HASH암호화 방식
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + (60000*10)))    //만료시간 _ 언제까지 유효할 것인가 _ 현재시간 + 시간 10분
                                .withClaim("id", principalDetails.getUser().getId())
                                        .withClaim("username", principalDetails.getUser().getUsername())
                                                .sign(Algorithm.HMAC512("k"));
        response.addHeader("Authorization", "Bearer "+jwtToken);

//        super.successfulAuthentication(request, response, chain, authResult);
    }
}
