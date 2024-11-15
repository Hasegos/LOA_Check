package com.LostakTodo.lostakTodo.MemberShip;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller 
@RequiredArgsConstructor // 레포지토리 등록떄문에
public class MemberShipController {

    private final MemberShipService memberShipService; // User 레포지토리 등록

    private final PasswordEncoder passwordEncoder;

    // 비밀번호 8 ~ 20자리 영문자 / 숫자 포함
    private boolean isValidPassword(String password){
        return password !=  null && password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$");
    }

    @GetMapping("/membership")
    // 회원 가입페이지
    String MemberShip(){
        return "membership/membership.html";
    }

    @PostMapping("/membership")
    String MemberShip_Post(@RequestParam String password,String password_Again,
                           @Valid @ModelAttribute User user, BindingResult bindingResult, Model model) { // @Valid 어노테이션 사용해서 오류 메세지를 직접출력

        System.out.println(password);

        // 입력 데이터 문제 생겼을 시
        
        //  이메일 문제 있을시
        if (bindingResult.hasFieldErrors("userEmail")) {
            model.addAttribute("errorEmail", bindingResult.getFieldError("userEmail").getDefaultMessage());
            return "membership/membership.html";
        }
        // 비밀번호 문제 있을시
        if (!isValidPassword(password)) {
            model.addAttribute("errorPassword", "비밀번호는 8~20자리의 영문자와 숫자 조합이어야 합니다.");
            return "membership/membership.html";
        }

        // 비밀번호가 같지 않을시
        if (!password.equals(password_Again)) {
            model.addAttribute("errorPassword_Again", "비밀번호 확인을 입력해주세요");
            return "membership/membership.html";
        }
            // redirect 두번실행으로 오류가있음 <- 이미 작성한거에 덧붙이는거라 오류나는건데 이걸 다른방향으로 수정해보기
            // redirect 순서를 잡아주는 redirectAttributes 사용
            // 비밀번호 관련해서도 예외처리를 시킬거임

        
        // 데이터 저장 처리
        try {
            var hashing = passwordEncoder.encode(password); // 비밀번호 해싱
            memberShipService.membership(user.getUserEmail() , hashing);
        }
        catch (Exception e){
            model.addAttribute("", "회원 가입중에 오류가 발생했습니다");
            return "membership/membership.html";
        }


        return "home/home.html"; // 정상적으로 저장시 메인페이지 이동
    }

}
