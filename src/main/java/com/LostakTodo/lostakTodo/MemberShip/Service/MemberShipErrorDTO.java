package com.LostakTodo.lostakTodo.MemberShip.Service;

import lombok.Getter;


@Getter
// 실패시 검증 결과를 담기위해 사용하는 DTO
public class MemberShipErrorDTO {

    private boolean valid; // 검증 결과 
    private String fieldName; // 문제가 있는 필드 명
    private String errorMessage; // 에러 메세지

    public MemberShipErrorDTO(boolean valid, String fieldName , String errorMessage){
        this.valid = valid;
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }
    
    // 검증 성공했을시
    public static MemberShipErrorDTO success() {
        return new MemberShipErrorDTO(true, null, null);
    }

    // 검증 실패시
    public static MemberShipErrorDTO failure(String fieldName , String errorMessage){
        return new MemberShipErrorDTO(false, fieldName, errorMessage);
    }
}