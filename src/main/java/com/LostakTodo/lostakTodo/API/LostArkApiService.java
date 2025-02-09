package com.LostakTodo.lostakTodo.API;

import com.LostakTodo.lostakTodo.MemberShip.UserData.User;
import com.LostakTodo.lostakTodo.MemberShip.UserData.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LostArkApiService {

    // api
    private  final ObjectMapper objectMapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final UserApiNameRepository userApiNameRepository;

    @Value("${game.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate = new RestTemplate();


    public List<String> processCharacterInfo(String json, Authentication auth) {
        List<String> characterLevels = new ArrayList<>();

        Optional<User> result = userRepository.findAllByUserEmail(auth.getName());
        User User_Information = result.get(); // 유저 정보 가져오기
        // 이후에 유저 정보중에 id를 이용해서 api, 닉네임 정보 저장 테이블 찾기
        Optional<UserApiName> Api_Information = userApiNameRepository.findAllByUserId(User_Information.getId());

        // 데이터 쿼리문 가져오는게 너무늦음 그리고 이미지를 png 나 jpg로 바꿔서 보여주게끔
        

        try{            
            JsonNode rootNode = objectMapper.readTree(json);
            
            var UserName_Rep = Api_Information.get().getUserName(); // 유저가 저장한 대표캐릭
            var APIKey = Api_Information.get().getApiKey(); // API키

            var UserName_Rep_Server = getCharacterImage(UserName_Rep, APIKey).get("server"); //  대표 캐릭터 서버이름 정보



            System.out.println(getCharacterImage(UserName_Rep, APIKey).get("server"));

            for(JsonNode characterNode : rootNode){
                String characterName = characterNode.path("CharacterName").asText();
                String level = characterNode.path("CharacterLevel").asText();
                String image = getCharacterImage(characterName, APIKey).get("image"); // DB에 저장된 api 키 가져오기
                String lastLogin = characterNode.path("LastLogin").asText(); // 마지막 로그인 시점

                System.out.println(getCharacterImage(characterName, APIKey).get("server"));

                String server = getCharacterImage(characterName, APIKey).get("server");

                // 대표 캐릭터의 해당되는 서버 캐릭터만 가져오기
                if(!UserName_Rep_Server.equals(server)){
                    System.out.print("대표 캐릭터의 서버와 맞지않습니다.");
                    continue;
                }

                // 레벨 낮으면 이미지조차도 안불러와줘짐
                if(Integer.parseInt(level) <= 10 || image == null || image.isEmpty()){
                    System.out.println("불러 올 수 없는 정보");
                    continue;
                }

                String detail = "캐릭터 이름 : " +  characterName +
                "레벨 : " + level +
                "프로필 이미지" + image;
                characterLevels.add(detail);
            }
            return characterLevels;
        }catch (Exception e){
            e.printStackTrace();
            characterLevels.add("오류 처리 중 문제발생");
            return characterLevels;
        }

    }

    // 캐릭터의 서버와 이름 가져오기
    /**/
    public Map<String, String> getCharacterImage(String playerId, String apiKey) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl + "/armories/characters/" + playerId + "/profiles")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        Map<String, String> result = new HashMap<>();

        int retryCount = 0;
        int maxRetries = 3;


        while (retryCount < maxRetries) {
            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);


                JsonNode rootNode = objectMapper.readTree(response.getBody());
                String profileImageUrl = rootNode.path("CharacterImage").asText();
                String profileServerName = rootNode.path("ServerName").asText();

                result.put("image", profileImageUrl);
                result.put("server", profileServerName);


                return result;

            } catch (HttpClientErrorException.TooManyRequests e) {
                retryCount++;
                System.out.println("429 오류 발생: 요청이 너무 많음. 잠시 후 다시 시도.");
                try {
                    Thread.sleep(2000);

                } catch (InterruptedException ignored) {}
            } catch (Exception e) {
                e.printStackTrace();
                return Map.of("error", " 해당캐릭터 프로필은 없습니다.");
            }
        }
        return Map.of("error", "요청 제한 초과 (429 오류)");
    }

}
