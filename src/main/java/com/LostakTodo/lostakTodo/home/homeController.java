package com.LostakTodo.lostakTodo.home;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class homeController {

    @GetMapping("/home")
    String home(){
        return "/home/home.html";
    }
}
