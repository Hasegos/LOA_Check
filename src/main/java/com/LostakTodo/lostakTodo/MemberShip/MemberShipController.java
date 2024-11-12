package com.LostakTodo.lostakTodo.MemberShip;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller 
@RequiredArgsConstructor // 레포지토리 등록떄문에
public class MemberShipController {
    private final MemberShipService memberShipService; // User 레포지토리 등록


    @GetMapping("/membership")
    // 회원 가입페이지
    String MemberShip(){
        return "membership/membership.html";
    }

    @PostMapping("/membership")
    String MemberShip_Post(String password_Again,
                           @Valid User user, BindingResult bindingResult, Model model) { // @Valid 어노테이션 사용해서 오류 메세지를 직접출력

        try{
            if(bindingResult.hasErrors() || !user.getPassword().equals(password_Again)){

                if(bindingResult.hasErrors()){
                    model.addAttribute("errorEmail", bindingResult.getFieldError("userEmail").getDefaultMessage());
                }
                // User에 적어놓은 @Email에있는 오류 메세지 출력
                if(!user.getPassword().equals(password_Again)){
                    model.addAttribute("errorPassword_Again", "비밀번호 확인을 입력해주세요");
                }
               // redirect 두번실행으로 오류가있음 <- 이미 작성한거에 덧붙이는거라 오류나는건데 이걸 다른방향으로 수정해보기
                // redirect 순서를 잡아주는 redirectAttributes 사용
                // 비밀번호 관련해서도 예외처리를 시킬거임
                return "membership/membership.html";
            }

            else{
                memberShipService.membership(user.getUserEmail(), user.getPassword());
                return "home/home.html";
            }
        }catch (Exception e){

            return "redirect:/membership";
        }
    }
}
