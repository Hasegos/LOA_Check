package com.LostakTodo.lostakTodo.API;

import com.LostakTodo.lostakTodo.MemberShip.UserData.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserApiName {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // api 키 길이 천글자 이하로 설정
    @Column(length = 1000)
    private String apiKey;

    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
