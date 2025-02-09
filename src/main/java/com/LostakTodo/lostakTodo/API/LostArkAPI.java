package com.LostakTodo.lostakTodo.API;


import com.LostakTodo.lostakTodo.MemberShip.UserData.User;
import com.LostakTodo.lostakTodo.MemberShip.UserData.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

// 로스트아크 API 키 가져오기
@Service
@RequiredArgsConstructor

public class LostArkAPI {

    private final UserApiNameRepository userApiNameRepository;
    private final UserRepository userRepository;
    // application 에 저장된 설정값을 하드 코딩없이 가져올수있게 설정해주는 어노테이션
    @Value("${game.api.url}")
    private String apiUrl;


    // api 키와 플레리어 닉네임 저장
    public void setApiKey(String apiKey, Authentication auth, String playerId){

        try {
            // User 에 있는 정보를 찾기
            Optional<User> result = userRepository.findAllByUserEmail(auth.getName());
            User user = result.get();
            
            // Api 테이블에 user 정보를 외래참조로 저장 후에 api 키와 닉네임 저장
            UserApiName userApiName = new UserApiName();

            userApiName.setUser(user);
            userApiName.setApiKey(apiKey);
            userApiName.setUserName(playerId);

            userApiNameRepository.save(userApiName);
        }catch (Exception e){
            System.out.println("에러 메세지" + e.getMessage());
        }
    }

    // HTTP 메서드(GET, POST, PUT, DELETE 등) 처리할수있게 해줌
    private final RestTemplate restTemplate = new RestTemplate();

    private final LostArkApiService lostArkApiService;

    // 유저 통합 정보
    public List<String> getPlayerData(String playerId, String apiKey , Authentication auth){

        // UriComponentsBuilder.fromHttpUrl 를사용해서 기본적인 URL에 동적으로 경로를 추가
        // 해당 경로는 로스트아크 api키 사용법에 있음
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl + "/characters/" + playerId + "/siblings")
                .toUriString();
        // 인증정보나 기타 요청에 필요한 정보를 전달하기위해서
        // HTTP 헤더는 클라이언트와 서버 간의 요청과 응답에 추가적인 정보 제공(특정정보를 헤더에 포함가능)
        // 로스트아크 API 특성상 Authorization 권한과 Bearer 토근에 api키를 같이넣어줘야함
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        // 헤더 정보를 포함시켜 외부 api에 요청하기위해서
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            /* 캐릭터 정보를 List 형태로 넘겨주기 (html에서 타임리프로 쪼개서 정보를 출력) */
            List<String> characterInfo = lostArkApiService.processCharacterInfo(response.getBody() ,auth);

            return characterInfo;
        }catch(Exception e){
            e.printStackTrace();
            return Collections.singletonList("해당 캐릭터 정보가 없습니다.");
        }
    }

}
