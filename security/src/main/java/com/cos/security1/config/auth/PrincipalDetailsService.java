package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * 시큐리티 설정에서 loginPorcessingUrl("/login");
 * /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 함수가 실행
 * 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어지는곳
 */


@Service
public class PrincipalDetailsService implements UserDetailsService {

    //시큐리티 session = Authentication = UserDetails
    //시큐리티 세션 (내부 Authentication(내부  UserDetails))로 이루어지게 되는것
   @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);

        if(userEntity != null){
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
