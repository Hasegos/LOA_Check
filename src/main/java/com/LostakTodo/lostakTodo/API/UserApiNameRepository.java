package com.LostakTodo.lostakTodo.API;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserApiNameRepository extends JpaRepository<UserApiName, Long> {

    Optional<UserApiName> findAllByUserId(Long user_id);
}
