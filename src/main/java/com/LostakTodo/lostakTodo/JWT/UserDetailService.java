package com.LostakTodo.lostakTodo.JWT;

import com.LostakTodo.lostakTodo.MemberShip.User;
import com.LostakTodo.lostakTodo.MemberShip.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
// UserDetailsService 를 이용해서 user 에 대한 정보 수정
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        Optional<User> result = userRepository.findAllByUserEmail(userEmail);
         if(result.isEmpty()){
             throw new UsernameNotFoundException("그런 아이디 없습니다.");
         }
        var user_Information =  result.get();
        System.out.println(result.get()); // 해당 유저에대한 정보


        List<GrantedAuthority> authorities = new ArrayList<>(); // 리스트 타입으로 권한 수정하게끔 생성
        authorities.add(new SimpleGrantedAuthority("일반유저")); // 관리자 권한도 따로 만들어줘야함
        var user = new CustomUser(user_Information.getUserEmail(), user_Information.getPassword() , authorities ); // 권한도 추가
        user.id = user_Information.getId();

        return user; // 권한 , 아이디 , 이메일 , 비번이 들어가있는 정보를 돌려줌
    }
}