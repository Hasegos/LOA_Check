package com.LostakTodo.lostakTodo.Security;

import com.LostakTodo.lostakTodo.JWT.JWTFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        // 비밀번호를 안전하게 해시화 후 암호화상태로 저장 / 검사
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        // CSFR 공격 방어를 위해 세션 기반으로 CSRF 토큰을 생성하고 관리
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Bean // 어떤페이지를 로그인 검사할지 설정가능
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable()); // 개발을 위해 csrf 끄기

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );  // JWT를 사용할거기에  세션 데이터 생성 금지

        //로그인에 대해 유효한지 검사를 하기위해서 Filter 사용 -> Fillter 관련 클래스 따로뺴기

        http.addFilterBefore(new JWTFilter(), ExceptionTranslationFilter.class);


        // 모든 경로에 대해 인증을 요구 X (만약 필요하면 URL에 주소넣기
        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/**").permitAll()
        );
        
        // 로그아웃 하면 로그인 서버로 보내기
        http.logout(logout -> logout.logoutUrl("/logout")
                .logoutSuccessUrl("/login")
        );

        return http.build();
    }

}

