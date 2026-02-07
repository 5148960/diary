package com.example.diary1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호를 암호화하지 않고 평문으로 저장 (테스트용)
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 사이트 간 요청 위조 방지 비활성화 (개발 편의상)
        http.csrf(csrf -> csrf.disable());

        // 접근 권한 설정
        http.authorizeHttpRequests(auth -> auth
                // 로그인, 회원가입, 스타일 파일은 로그인 없이도 가능
                .requestMatchers("/login", "/join", "/css/**", "/js/**", "/images/**").permitAll()
                // 그 외 모든 페이지(메인 달력 등)는 무조건 로그인 필수!
                .anyRequest().authenticated()
        );

        // 로그인 설정
        http.formLogin(form -> form
                .loginPage("/login")             // 커스텀 로그인 페이지 주소
                .defaultSuccessUrl("/", true)    // 로그인 성공 시 메인으로 이동
                .permitAll()
        );

        // 로그아웃 설정
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")      // 로그아웃하면 로그인 창으로 튕김
                .invalidateHttpSession(true)     // 세션 삭제
                .deleteCookies("JSESSIONID")     // 쿠키 삭제
                .permitAll()
        );

        return http.build();
    }
}