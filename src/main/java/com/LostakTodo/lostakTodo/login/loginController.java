package com.LostakTodo.lostakTodo.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class loginController {

    // 로그인페이지
    @GetMapping("/login")
    String Login(){
        return "login/login.html";
    }

    @PostMapping("/login")
    String Login_Post(@RequestBody Map<String, String> data){

        return "redirect:/login";
    }
}
