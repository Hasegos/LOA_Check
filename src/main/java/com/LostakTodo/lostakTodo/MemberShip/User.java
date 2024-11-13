package com.LostakTodo.lostakTodo.MemberShip;

import com.LostakTodo.lostakTodo.MemberShip.domain.AllowedDomainEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 1씩 증가
    private Long id;

    @Column(unique = true) // 유일해야됨
    //@Email(message = "올바른 이메일 형식이 아닙니다") // 이메일 검증 이게 @ 치고 뒤에 아무문자와도 문제를 안잡아줌
    //그래서 커스텀 해야됨

    @AllowedDomainEmail(domains = {"naver.com" , "daum.net", "gmail.com"},
            message = "올바른 이메일 형식이 아닙니다.") // <- 해당 이메일 주소를 배열로 받는 커스텀 어노테이션 사용
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    private String userEmail; //유저 이메일

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
            message = "비밀번호는 8~20자리의 영문자와 숫자 조합이어야 합니다.")
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password; // 비번
}
