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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LostArkAPIController {

    private final LostArkAPI lostArkAPI;
    private final UserApiNameRepository userApiNameRepository;
    private final UserRepository userRepository;

    @GetMapping("/api")
    String API(){
        return "API/API.html";
    }

    // homework(숙제) 를 눌렀을시 요청
    @GetMapping("/homework")
    String homework(Model model, Authentication auth){

        try {
            // 닉네임
            String playerId = get_APi(auth).get().getUserName();
            // api 키
            String ApiKey = get_APi(auth).get().getApiKey();
            
            // 만약 api 가 올바르지않을 경우 DB에 저장 X
            if (lostArkAPI.getPlayerData(playerId, ApiKey,auth).equals("오류발생")) {
                System.out.println("API 키가 올바르지않습니다.");
                return "home/home.html";
            } else {
                model.addAttribute("playerId", lostArkAPI.getPlayerData(playerId, ApiKey,auth));
                return "homework/homework.html";
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "home/home.html";
        }

    }
    // API 키 등록시 DB에 등록
    @PostMapping("/homework")
    String API_Get(@RequestParam String API_key, String playerId, Model model, Authentication auth){

        // 존재하지않을 경우
        if(!get_APi(auth).isPresent()){
            lostArkAPI.setApiKey(API_key, auth, playerId);
            model.addAttribute("playerId",lostArkAPI.getPlayerData(playerId,API_key,auth));
            return "homework/homework.html";
        }
        // 이미 존재할경우 얻데이트
        if(get_APi(auth).isPresent()){
            UserApiName remove = get_APi(auth).get();
            userApiNameRepository.delete(remove);
        }
        
        lostArkAPI.setApiKey(API_key, auth, playerId);
        List<String> player = lostArkAPI.getPlayerData(playerId,API_key,auth);
        System.out.println(player);
        model.addAttribute("playerId",player);
        return "homework/homework.html";
    }
    
    // DB에 저장된 Api 키 가져오는 함수
    public Optional<UserApiName> get_APi(Authentication auth){
        
        Optional<User> result = userRepository.findAllByUserEmail(auth.getName());
        User User_Information = result.get(); // 유저 정보 가져오기
        // 이후에 유저 정보중에 id를 이용해서 api, 닉네임 정보 저장 테이블 찾기
        Optional<UserApiName> Api_Information = userApiNameRepository.findAllByUserId(User_Information.getId());

        return Api_Information;
    }
}
