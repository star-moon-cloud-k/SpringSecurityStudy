package com.cos.security1.config.auth;


import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴
 * 로그인을 진행이 완료가 되면 session을 만들어준다.  : (Security ContextHolder)세션 정보를 저장함
 * 세션에 들어갈 수 있는 오브젝트가 정해져있음 => Authentication 타입의 객체
 * Authentication 안에 User정보가 있어야함.
 * User 오브젝트 타입 => Authentication 안의 UserDetails 타입객체가 된다
 * <p>
 * Security Session => Authentication => UserDetails
 */


@Data
public class PrincipalDetails implements UserDetails {

    private User user;     // 콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
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
}
