package com.LostakTodo.lostakTodo.API;

import com.LostakTodo.lostakTodo.MemberShip.UserData.User;
import com.LostakTodo.lostakTodo.MemberShip.UserData.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LostArkAPIController {
    

    private final LostArkAPI lostArkAPI;

    private String playerId;
    private final UserApiNameRepository userApiNameRepository;
    private final UserRepository userRepository;



    @GetMapping("/api")
    String API(){
        return "API/API.html";
    }


    // homework 관련 모음 (post 뿐만아니라 get요청시에도 내용나오게끔 수정)
    @GetMapping("/homework")
    String homework(Model model, Authentication auth){

        Optional<User> result = userRepository.findAllByUserEmail(auth.getName());

        var a = result.get();
        System.out.println(a.getId());


        var b = userApiNameRepository.findAllByUserId(a.getId());
        System.out.println(b.get().getUserName());
        playerId = b.get().getUserName();
        if(playerId == null){


            System.out.println("API 키 지금없음");
            System.out.println(auth.getPrincipal());
            return "home/home.html";
        }
        else {
            model.addAttribute("playerId", lostArkAPI.getPlayerData(playerId));
            return "homework/homework.html";
        }
    }

    @PostMapping("/homework")
    String API_Get(@RequestParam String API_key, String playerId, Model model, Authentication auth){
        lostArkAPI.setApiKey(API_key, auth, playerId);
        model.addAttribute("playerId",lostArkAPI.getPlayerData(playerId));

        return "homework/homework.html";
    }

}
