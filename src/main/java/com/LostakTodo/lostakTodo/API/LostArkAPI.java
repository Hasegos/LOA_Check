package com.LostakTodo.lostakTodo.API;



import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

// 로스트아크 API 키 가져오기
@Service
@RequiredArgsConstructor
public class LostArkAPI {

    // application 에 저장된 설정값을 하드 코딩없이 가져올수있게 설정해주는 어노테이션
    @Value("${game.api.key}")
    private String apiKey;

    @Value("${game.api.url}")
    private String apiUrl;
    
    // HTTP 메서드(GET, POST, PUT, DELETE 등) 처리할수있게 해줌
    private final RestTemplate restTemplate = new RestTemplate();

    private final LostArkApiService lostArkApiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getPlayerData(String playerId){
        
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
            List<String> characterInfo = lostArkApiService.processCharacterInfo(response.getBody());

            return String.join("\n", characterInfo);

        }catch(Exception e){
            e.printStackTrace();
            return "오류발생";
        }

    }

}
