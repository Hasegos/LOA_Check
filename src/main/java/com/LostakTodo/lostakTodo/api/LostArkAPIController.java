package com.LostakTodo.lostakTodo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LostArkAPIController {

    private final LostArkAPI lostArkAPI;
    private final UserApiNameRepository userApiNameRepository;
    private final LostArkApiService lostArkApiService;

    //
    @GetMapping("/api")
    String API(){
        return "API/API.html";
    }

    // homework(숙제) 를 눌렀을시 요청
    @GetMapping("/homework")
    String homework(Model model, Authentication auth){

        try {
            // 유저 정보 전체 불러내기
            UserApiName userApiInformation = lostArkApiService.get_APi(auth).get();
            // 닉네임
            String playerId = userApiInformation.getUserName();
            // api 키
            String ApiKey = userApiInformation.getApiKey();
            
            // 유저 통합 정보 가져오기
            String characterSiblings = lostArkAPI.getPlayerData(playerId, ApiKey,auth);
            // 만약 api 가 올바르지않을 경우 DB에 저장 X
            if (characterSiblings.equals("해당 유저정보는 없습니다.")) {
                System.out.println("API 키가 올바르지않습니다.");
                return "home/home.html";
            } else {
                model.addAttribute("Siblings", characterSiblings);
                return "homework/homework.html";
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "home/home.html";
        }

    }
    // API 키 등록시 DB에 등록
    @PostMapping("/homework") // 캐릭터 정보 추가 할떄 발생
    String API_Get(@RequestParam String API_key, String playerId, Model model, Authentication auth){

        // 존재하지않을 경우
        if(!lostArkApiService.get_APi(auth).isPresent()){
            lostArkAPI.setApiKey(API_key, auth, playerId);
            model.addAttribute("playerId",lostArkAPI.getPlayerData(playerId,API_key,auth));
            return "homework/homework.html";
        }
        // 이미 존재할경우 업데이트
        if(lostArkApiService.get_APi(auth).isPresent()){
            UserApiName remove = lostArkApiService.get_APi(auth).get();
            userApiNameRepository.delete(remove);
        }

        lostArkAPI.setApiKey(API_key, auth, playerId);

        model.addAttribute("playerId", lostArkAPI.getPlayerData(playerId,API_key,auth));
        return "homework/homework.html";
    }

}
