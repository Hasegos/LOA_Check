package com.LostakTodo.lostakTodo.MemberShip;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class MemberShipService {


    private final UserRepository userRepository;
    // 회원가입 설정
    @Transactional
    public void membership(String userEmail , String password){
        User user = new User();
        user.setUserEmail(userEmail);
        user.setPassword(password);
        userRepository.save(user);
    }
}
