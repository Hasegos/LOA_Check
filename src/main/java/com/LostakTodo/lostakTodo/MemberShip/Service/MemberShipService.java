package com.LostakTodo.lostakTodo.MemberShip.Service;

import com.LostakTodo.lostakTodo.MemberShip.UserData.User;
import com.LostakTodo.lostakTodo.MemberShip.UserData.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class MemberShipService {


    private final UserRepository userDataRepository;
    private  final PasswordEncoder passwordEncoder; // 비밀번호 해싱을 위해서

    // 회원가입 설정
    public MemberShipErrorDTO memberShipValid(String password,
                                              String password_Again, BindingResult bindingResult) {
        
        // 해결해야되는 문제 : 이메일이 동일할수는 있는데 비번이 같은 경우 -> 이거 사용안되게끔
        
        /// 이메일에 문제가 있을 시
        if (bindingResult.hasFieldErrors("userEmail")) {
            return MemberShipErrorDTO.failure("Email", bindingResult.getFieldError("userEmail").getDefaultMessage());

        }
        // 비밀번호 문제가 있을 시
        if(bindingResult.hasFieldErrors("rawPassword")){
            return MemberShipErrorDTO.failure("Password", bindingResult.getFieldError("rawPassword").getDefaultMessage());
        }

        // 비밀번호가 같지 않을시
        if (!password.equals(password_Again)) {
            return MemberShipErrorDTO.failure("Password_Again", "비밀번호 확인후 입력해주세요");
        }

        return MemberShipErrorDTO.success();
    }

    public void membership(String userEmail, String password, User userData){

        // var hashing = passwordEncoder.encode(password); // 비밀번호 해싱
        userData.setHashedPassword(password);
        userData.setUserEmail(userEmail);
        userDataRepository.save(userData);
    }
}

