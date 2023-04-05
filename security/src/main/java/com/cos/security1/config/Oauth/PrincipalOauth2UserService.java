package com.cos.security1.config.Oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    /**
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
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("UserRequest getClientRegistration" + userRequest.getClientRegistration());
        System.out.println("UserRequest getAccessToken" + userRequest.getAccessToken().getTokenValue());        //registrationID로 어떤 OAuth로 로그인 헸는지 확인가능
        System.out.println("UserRequest getClientRegistration" + userRequest.getClientRegistration());
        System.out.println("getAttribute" +  super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        //회원가입을 진행예정
        return super.loadUser(userRequest);
    }
}
