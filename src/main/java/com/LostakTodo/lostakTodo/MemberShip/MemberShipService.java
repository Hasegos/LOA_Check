package com.LostakTodo.lostakTodo.MemberShip;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;

@Service
@RequiredArgsConstructor
public class MemberShipService {

    private final UserRepository userRepository;
    // 회원가입 설정
    public void membership(String userEmail, String password){

        User user = new User();
        user.setUserEmail(userEmail);
        user.setPassword(password);
        userRepository.save(user);

    }
}
