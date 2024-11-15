package com.LostakTodo.lostakTodo.MemberShip;

import com.LostakTodo.lostakTodo.MemberShip.domain.AllowedDomainEmail;
import com.LostakTodo.lostakTodo.MemberShip.password.AllowedPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 1씩 증가
    private Long id;

    //@Email(message = "올바른 이메일 형식이 아닙니다") // 이메일 검증 이게 @ 치고 뒤에 아무문자와도 문제를 안잡아줌
    //그래서 커스텀 해야됨
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @AllowedDomainEmail(domains = {"naver.com" , "daum.net", "gmail.com"},
            message = "올바른 이메일 형식이 아닙니다.") // <- 해당 이메일 주소를 배열로 받는 커스텀 어노테이션 사용
    private String userEmail; //유저 이메일

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.") // 정규식 표현으로는 계속오류남 -> 나중을 위해 커스텀 어노테이션 사용
   //  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "비밀번호는 8~20자리의 영문자와 숫자 조합이어야 합니다.")

//    @AllowedPassword(message = "비밀번호는 8~20자리의 영문자와 숫자 조합이어야 합니다. ") // false일 때 사용
//    private  transient String rawPassword;
//
//    private String hashedPassword; // 비번

    private String password; // 비밀번호
}
