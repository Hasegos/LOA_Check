package com.LostakTodo.lostakTodo.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LostArkApiService {

    private  final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> processCharacterInfo(String json) {
        List<String> characterLevels = new ArrayList<>();

        try{
            JsonNode rootNode = objectMapper.readTree(json);

            for(JsonNode characterNode : rootNode){
                String characterName = characterNode.path("CharacterName").asText();
                String level = characterNode.path("CharacterLevel").asText();

                String detail = "캐릭터 이름 : " + characterName + ", 레벨 :" + level;
                characterLevels.add(detail);
            }


            return characterLevels;
        }catch (Exception e){
            e.printStackTrace();
            characterLevels.add("오류 처리 중 문제발생");
            return characterLevels;
        }

    }

}
