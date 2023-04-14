package com.example.jwt.config.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("필터1");

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;


        /**
         * Id, PW가 정상적으로 들어와서 로그인이 되는 경우 토큰을 만들어주고 응답해줌
         * 요청할 떄 마다 header에 Authorization 에서 value 값으로 토큰을 던져줌
         * 그 때 토큰이 넘어오면 토큰이 적절한 토큰인지 검증 (RSA , HS256)
         */
        if(req.getMethod().equals("POST")){
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            if(headerAuth.equals("hello")){
                filterChain.doFilter(req,res);
            }else {
                PrintWriter out = res.getWriter();
                out.println("인증 안됨");       //인증이 안된 경우 return 해줌 필터에서 체이닝이 끊어지고 차단됨.
            }

        }

//        filterChain.doFilter(servletRequest, servletResponse);
    }
}
