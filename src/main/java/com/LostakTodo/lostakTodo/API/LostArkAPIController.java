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
    private final UserApiNameRepository userApiNameRepository;
    private final UserRepository userRepository;

    // DB에 저장된 데이터 이용하기 (프로그램 실행했을경우 다른 호스팅이나 get 을 받았을때 해당값을 까먹음)
    private String playerId;
    private String ApiKey;

    @GetMapping("/api")
    String API(){
        return "API/API.html";
    }

    // homework 관련 모음 (post 뿐만아니라 get요청시에도 내용나오게끔 수정)
    @GetMapping("/homework")
    String homework(Model model, Authentication auth){

        Optional<User> result = userRepository.findAllByUserEmail(auth.getName());

        User User_Information = result.get(); // 유저 정보 가져오기

        // 유저 정보에있는 아이디와 api 에 있는 외래참조 키를 이용해서 정보찾기
        Optional<UserApiName> Api_Information = userApiNameRepository.findAllByUserId(User_Information.getId());

        playerId = Api_Information.get().getUserName();
        ApiKey = Api_Information.get().getApiKey();

        if(playerId == null){
            System.out.println("API 키 지금없음");
            return "home/home.html";
        }
        else {
            System.out.println(lostArkAPI.getPlayerData(playerId,ApiKey));
            model.addAttribute("playerId", lostArkAPI.getPlayerData(playerId,ApiKey));
            return "homework/homework.html";
        }
    }

    @PostMapping("/homework")
    String API_Get(@RequestParam String API_key, String playerId, Model model, Authentication auth){

        Optional<User> result = userRepository.findAllByUserEmail(auth.getName());

        User User_Information = result.get(); // 유저 정보 가져오기

        Optional<UserApiName> Api_Information = userApiNameRepository.findAllByUserId(User_Information.getId());

        if(!Api_Information.isPresent()){
            lostArkAPI.setApiKey(API_key, auth, playerId);

            model.addAttribute("playerId",lostArkAPI.getPlayerData(playerId,API_key));
            return "homework/homework.html";
        }
        // 이미 존재할경우 얻데이트
        if(Api_Information.isPresent()){

            UserApiName remove = Api_Information.get();
            userApiNameRepository.delete(remove);

        }
        lostArkAPI.setApiKey(API_key, auth, playerId);

        model.addAttribute("playerId",lostArkAPI.getPlayerData(playerId,API_key));
        return "homework/homework.html";
    }
}
