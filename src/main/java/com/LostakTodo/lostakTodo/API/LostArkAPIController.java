package com.LostakTodo.lostakTodo.API;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LostArkAPIController {

    @GetMapping("/api")
    String API(){
        return "API/API.html";
    }

}
