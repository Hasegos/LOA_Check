package com.LostakTodo.lostakTodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LostakTodoApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(LostakTodoApplication.class, args);
		// 정상적인 실행여부 확인
		System.out.println("LOA check 실행중");
	}

}