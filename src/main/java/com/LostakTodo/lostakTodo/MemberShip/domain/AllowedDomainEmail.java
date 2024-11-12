package com.LostakTodo.lostakTodo.MemberShip.domain;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedDomainEmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedDomainEmail {
    String message() default "허용된 도메인 이메일 주소가 아닙니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] domains(); // 허용하는 도메인이 구글 / 네이버 / 다음 정도만 일단 해보기로
}
