package com.LostakTodo.lostakTodo.API;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LostArkAPIController {

    private final LostArkAPI lostArkAPI;

    @GetMapping("/api")
    String API(){
        return "API/API.html";
    }


    @PostMapping("/api")
    String API_Get(@RequestParam String API_key, String playerId){

        lostArkAPI.setApiKey(API_key);
        System.out.println(lostArkAPI.getPlayerData(playerId)); // 메인페이지에 캐릭터 정보뜨게하기


        return "home/home.html";
    }

}
