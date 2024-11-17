package com.LostakTodo.lostakTodo.MemberShip.Custom_Password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedPasswordValidator.class) // 해당클래스에서 조건
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedPassword {
    String message() default "비밀번호는 8~20자리의 영문자와 숫자 조합이어야 합니다.";
    Class<?> [] groups() default {} ;
    Class<? extends Payload>[] payload() default {};
}
