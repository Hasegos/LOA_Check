package com.LostakTodo.lostakTodo.api;

import com.LostakTodo.lostakTodo.MemberShip.UserData.User;
import com.LostakTodo.lostakTodo.MemberShip.UserData.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@EnableCaching
public class LostArkApiService {

    // api
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final UserApiNameRepository userApiNameRepository;


    @Value("${game.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate = new RestTemplate();


    public List<String> processCharacterInfo(String jsonPlayerId, Authentication auth) {
        List<String> charactersCollection = new ArrayList<>();

        // 유저가 가지고있는 API 키 불러오기
        Optional<UserApiName> userInformation = get_APi(auth);
        String userAPIKey = userInformation.get().getApiKey(); // API 키
        String UserCharacterDaepyoName = userInformation.get().getUserName(); // 유저가 저장한 대표캐릭

        try{
            JsonNode rootNode = objectMapper.readTree(jsonPlayerId);

            String UserCharacterDaepyoServer = "";
            String characterServer , characterName , level;

            for(JsonNode characterNode : rootNode){
                if(characterNode.path("CharacterName").asText().equals(UserCharacterDaepyoName)){
                    UserCharacterDaepyoServer = characterNode.path("ServerName").asText();
                    break;
                }
            }

            for(JsonNode characterNode : rootNode){
                // 서버가 같은지 먼저 거르고 나서 후에 데이터 가져오기
                characterServer = characterNode.path("ServerName").asText();
                // String lastLogin = characterNode.path("LastLogin").asText(); // 마지막 로그인 시점

                // 대표 캐릭터의 해당되는 서버 캐릭터만 가져오기
                if(!UserCharacterDaepyoServer.equals(characterServer)){
                    System.out.println("대표 캐릭터의 서버와 맞지않습니다.");
                    continue;
                }
                
                characterName = characterNode.path("CharacterName").asText();
                level = characterNode.path("CharacterLevel").asText();
                // API 키와 캐릭터 이름 넘겨주고 이미지 가져오기
                String image = getCharacterImage(characterName, userAPIKey);

                // 레벨 낮으면 이미지조차도 안불러와줘짐
                if(Integer.parseInt(level) <= 10 || image == null || image.isEmpty()){
                    System.out.println("불러 올 수 없는 정보");
                    continue;
                }

                // 서버이름 , 캐릭터 이름 , 레벨 , 이미지 보내기
                String characterInformation = "서버 : " + characterServer +
                "\n캐릭터 이름 : " +  characterName +
                "\n레벨 : " + level +
                "\n프로필 이미지" + image;
                // 이때 "\n"는 html 에서 문자로 인식하기에 띄워쓰기가 안됨으로 <br>태그로 변경
                characterInformation = characterInformation.replace("\n", "<br>");

                charactersCollection.add(characterInformation);
            }
            return charactersCollection;
        }catch (Exception e){
            e.printStackTrace();
            charactersCollection.add("오류 처리 중 문제발생");
            return charactersCollection;
        }
    }

    // 캐릭터의 이미지 가져오기
    // 비동기 처리, 캐싱작업
    @Async
    @Cacheable(value = "CharacterImage", key = "#playerId")
    public String getCharacterImage(String playerId, String apiKey) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl + "/armories/characters/" + playerId + "/profiles")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String imageURL = rootNode.path("CharacterImage").asText();
            // 캐릭터 이미지 주소
            return imageURL;
        }catch (Exception e) {
            e.printStackTrace();
            return "이미지 없음";
        }
    }

    // DB에 저장된 Api 키 가져오는 함수
    public Optional<UserApiName> get_APi(Authentication auth){
        
        // 유저가 로그인한 정보의 이메일 정보로 유저 정보 찾기
        Optional<User> userLine = userRepository.findAllByUserEmail(auth.getName());
        // 이후에 유저 정보중에 id를 이용해서 api, 닉네임 정보 저장 테이블 찾기
        Optional<UserApiName> userInformation = userApiNameRepository.findAllByUserId(userLine.get().getId());

        return userInformation;
    }
}
