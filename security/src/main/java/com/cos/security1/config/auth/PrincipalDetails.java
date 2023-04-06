package com.cos.security1.config.auth;


import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴
 * 로그인을 진행이 완료가 되면 session을 만들어준다.  : (Security ContextHolder)세션 정보를 저장함
 * 세션에 들어갈 수 있는 오브젝트가 정해져있음 => Authentication 타입의 객체
 * Authentication 안에 User정보가 있어야함.
 * User 오브젝트 타입 => Authentication 안의 UserDetails 타입객체가 된다
 * Security Session => Authentication => UserDetails
 *
 * Oauth와 일반 로그인 두 정보를 확인 가능 하게 설정
 */

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;     // 콤포지션
    private Map<String, Object> attributes;

    //일반 로그인 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }
    //Oauth 로그인 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }


    /**
     * OAuth2User
     *
     * * sub=107268518070160694243        : 구글에 등록되어있는 ID
     *      *      * name=dh k                        : 이름
     *      *      * given_name=dh
     *      *      * family_name=k
     *      *      * picture=https://lh3.googleusercontent.com/a/AGNmyxYSGgrq17dAIU5XaY7UGdcQPZotoQVjvdBHlO2Gxg=s96-c
     *      *      * email=kspecial44@gmail.com
     *      *      * email_verified=true
     *      *      * locale=ko
     * @param name
     * @return
     * @param <A>
     */
    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    /**
     * OAuth2User
     * @return
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    //해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();

        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        //계정의 휴면 계정 여부
        return true;
    }

    /**
     * OAuth2User
     * attributes.get("sub"); 로 반환하게 생성
     * 별로 중요하지 않음 - 사용 안한다고 함
  함  * @return
     */
    @Override
    public String getName() {
        return attributes.get("sub").toString();
    }
}
