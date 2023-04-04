package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


//CRUD 함수를 들고있음
//@Repository 어노테이션이 없어도 IoC됨
//
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
