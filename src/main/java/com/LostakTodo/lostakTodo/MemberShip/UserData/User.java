package com.LostakTodo.lostakTodo.MemberShip.UserData;

import com.LostakTodo.lostakTodo.MemberShip.Custom_domain.AllowedDomainEmail;
import com.LostakTodo.lostakTodo.MemberShip.Custom_Password.AllowedPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 1씩 증가
    private Long id;
    
    // 커스텀 어노테이션 사용해야하는 이유
    /*
    @Email(message = "올바른 이메일 형식이 아닙니다") // 이메일 검증 이게 @ 치고 뒤에 아무문자와도 문제를 안잡아줌
    그래서 커스텀 해야됨
     */

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @AllowedDomainEmail(domains = {"naver.com" , "daum.net", "gmail.com"}, // <- 해당 이메일 주소를 배열로 받는 커스텀 어노테이션 사용
            message = "올바른 이메일 형식이 아닙니다.") // 만약 이메일이 더필요하면 작성
    private String userEmail; //유저 이메일

    // @Transient 를 사용해서 JPA 에서 직접적으로 서버에 관리 안하게끔 설정 즉, 입력값만 가져와서 확인해보고
    // 실질적인 데이터는 hashedPassword 에 저장

    @Transient
    @AllowedPassword(message = "비밀번호는 8~20자리의 영문자와 숫자 조합이어야 합니다. ") // false일 때 사용
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String rawPassword; // 입력된 원본 비번

    private String hashedPassword; // 해싱된 비번
}
