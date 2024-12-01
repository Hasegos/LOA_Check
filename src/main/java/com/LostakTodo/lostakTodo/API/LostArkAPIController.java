package com.LostakTodo.lostakTodo.API;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LostArkAPIController {
    

    private final LostArkAPI lostArkAPI;

    private String playerId;


    @GetMapping("/api")
    String API(){
        return "API/API.html";
    }


    // homework 관련 모음 (post 뿐만아니라 get요청시에도 내용나오게끔 수정)
    @GetMapping("/homework")
    String homework(Model model){
        model.addAttribute("playerId", lostArkAPI.getPlayerData(playerId));
        return "homework/homework.html";
    }

    @PostMapping("/homework")
    String API_Get(@RequestParam String API_key, String playerId, Model model){

        lostArkAPI.setApiKey(API_key);
        this.playerId = playerId;
        System.out.println(lostArkAPI.getPlayerData(playerId)); // 메인페이지에 캐릭터 정보뜨게하기
        model.addAttribute("playerId",lostArkAPI.getPlayerData(playerId));

        return "homework/homework.html";
    }

}
