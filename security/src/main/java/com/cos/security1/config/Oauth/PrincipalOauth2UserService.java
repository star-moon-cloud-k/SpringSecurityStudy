package com.cos.security1.config.Oauth;

import com.cos.security1.config.Oauth.provider.FacebookUserInfo;
import com.cos.security1.config.Oauth.provider.GoogleUserInfo;
import com.cos.security1.config.Oauth.provider.NaverUserInfo;
import com.cos.security1.config.Oauth.provider.OAuth2UserInfo;
import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private  UserRepository userRepository;


    /**
     * System.out.println("getAttribute" +  oauth2User.getAttributes());
     * userRequest 정보 : 구글 로그인 버튼 클릭 - 구글 로그인 창 - 로그인 완료 - code를 리턴 (OAuth-client 라이브러리) - AccessToken 요청
     * userReuqest 정보 - > 회원프로필 정보를 받아야함 ( loadUser함수 사용) -> 구글로 부터 회원 프로필을 받음
     * loadUser(userRequest).getAttributes()의 값을 보면
     *      * sub=107268518070160694243        : 구글에 등록되어있는 ID
     *      * name=dh k                        : 이름
     *      * given_name=dh
     *      * family_name=k
     *      * picture=https://lh3.googleusercontent.com/a/AGNmyxYSGgrq17dAIU5XaY7UGdcQPZotoQVjvdBHlO2Gxg=s96-c
     *      * email=kspecial44@gmail.com
     *      * email_verified=true
     *      * locale=ko
     * 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어지는곳
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("UserRequest getClientRegistration" + userRequest.getClientRegistration());
        System.out.println("UserRequest getAccessToken" + userRequest.getAccessToken().getTokenValue());        //registrationID로 어떤 OAuth로 로그인 헸는지 확인가능
        System.out.println("UserRequest getClientRegistration" + userRequest.getClientRegistration());
        System.out.println("getAttribute" +  super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        //회원가입을 진행
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));}
        else{
            System.out.println("구글과 페이스북과 네이버 지원");
        }
        System.out.println("getAttributes: " + oAuth2User.getAttributes());

//        String provider = userRequest.getClientRegistration().getRegistrationId();        //google
        String provider = oAuth2UserInfo.getProvider();        //google
//        String providerId = oAuth2User.getAttribute("sub");             //구글의 프로바이더 아이디
        String providerId = oAuth2UserInfo.getProviderId();
//        String providerId2 = oAuth2User.getAttribute("id");             //페이스북의 경우 id 사용 프로바이
//         String providerId = oAuth2User.getAttribute("sub");             //구글의 프로바이더 아이디더 아이디
//        String email = oAuth2User.getAttribute("email");
        String email = oAuth2UserInfo.getEmail();
        String username = provider+"_"+providerId;          //google_107268518070160694243      -> 유저 계정이 충돌날 일이 없어짐
        String password = bCryptPasswordEncoder.encode("아무 패스워어드");//비밀번호가 필요 없지만 일단 만들어주는 개념
        String role = "ROLE_USER";


        User userEntity = userRepository.findByUsername(username);        //Oath로 계정이 만들어져있는지 점검

        if(userEntity == null){
            System.out.println("구글 로그인 최초");
           userEntity = User.builder()
                   .username(username)
                   .password(password)
                   .email(email)
                   .role(role)
                   .provider(provider)
                   .providerId(providerId)
                   .build();
           userRepository.save(userEntity);
        }else{
            System.out.println("로그인 기록 있음, 계정 가입됨");
        }
//        return super.loadUser(userRequest);
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
