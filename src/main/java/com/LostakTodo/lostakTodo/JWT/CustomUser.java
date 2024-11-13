package com.LostakTodo.lostakTodo.JWT;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {

    public Long id;
    // 유저에대한 정보를 얻고싶을 걸 커스텀해버리면됨
    public CustomUser(String userEmail, String password, // 유저에대한 권한, 이메일, 비번을관리를 위해 사용 
                      Collection<? extends GrantedAuthority> authorities) {
        super(userEmail, password, authorities);
    }
}
