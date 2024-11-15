package com.LostakTodo.lostakTodo.MemberShip.password;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// 어노테이션 사용할려고 했으나 계속 된 데이터 저장때문에 로직을 안에서 작업
public class AllowedPasswordValidator implements ConstraintValidator<AllowedPassword, String> {

    @Override
    public  void  initialize(AllowedPassword constraintAnnotation ){ // 초기화 작업
        
    }

    @Override
    public boolean isValid(String rawPassword, ConstraintValidatorContext constraintValidatorContext) {

        if (rawPassword == null || rawPassword.length()  < 8 || rawPassword.length() > 20){
            return false;
        }
        
        // 영문자와 숫자가 있는 지 확인
        boolean hasLetter = false; // 영문자
        boolean hasDigit = false;  // 숫자
        
        for(char ch : rawPassword.toCharArray()){ // 입력 비번을 하나씩 반복문해서 비교
            if(Character.isLetter(ch)){ //
                hasLetter = true;
            }
            else if(Character.isDigit(ch)){
                hasDigit = true;
            }
        }

        if(hasDigit && hasLetter){
            return true;
        }
        return false;
    }
}
