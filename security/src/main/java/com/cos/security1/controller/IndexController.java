package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //localhost:8080/
    //localhost:8080

    /**
     * 일반 로그인 유저의 정보를 확인하는 방법
     * Authentication과 @AuthenticationPrincipal 두가지 방법으로 확인 가능
     * 스프링 시큐리티의 경우 시큐리티가 관리하는 세션 영역이 따로 존재함
     * 시큐리티 세션에서 관리하는 객체의 형식은 Authentication 의 객체만 가능
     * Authenticaion -> UserDetails 타입과  OAuth2User타입 두가지로 캐스팅 가능
     * UserDetail -> 일반 로그인
     * OAuth2User -> OAuth 로그인
     * @param authentication
     * @param userDetails
     * @return
     */
    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal UserDetails userDetails){       //DI 의존성 주입
        System.out.println("/test/login====================");
        System.out.println("authentication" + authentication.getPrincipal());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authetication : " + principalDetails.getUser().toString());

        System.out.println("userDetails: " + userDetails.getUsername());
        return "세션 정보 확인하기";
    }

    /**
     * OAuth로그인으로 유저 정보를 확인하는 방법
     * Authenticaion과 @AuthenticationPrincipal 두가지로 확인 가능
     * @param authentication
     * @return
     */
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth){       //DI 의존성 주입
        System.out.println("/test/login====================");
        System.out.println("authentication" + authentication.getPrincipal());
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); //OAuth로 검증할 때는 downcast가 안되고 오류 발생 _ 타입오류
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authetication : " + oAuth2User.getAttributes());
        System.out.println("oauth2user" + oAuth.getAttributes());

        return "Oauth 세션 정보 확인하기";
    }

    @GetMapping({"/" , ""})
    public String index(){
        //mustache 기본 폴더 src/main/resources/
        //뷰 리졸버 설정 : templates(prefix로 설정) .mustache(suffix)
        //
        return "index";     //src/main/resources/templates/index.mustache
    }
    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    @GetMapping("/loginForm")
    public  String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public  String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public  String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//    @PostAuthorize()
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터 정보";
    }

}
