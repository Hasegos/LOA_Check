package com.LostakTodo.lostakTodo.MemberShip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 유저 이메일이름을 이용해서 그 행전체를 찾는 기능
    Optional<User> findAllByUserEmail(String Email);
}
