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
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
        );

        http.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );

        // ⭐ 이 부분을 이렇게 수정하세요!
        http.logout(logout -> logout
                .logoutUrl("/logout")           // 로그아웃을 처리할 주소
                .logoutSuccessUrl("/login")     // 로그아웃하면 다시 로그인 화면으로!
                .invalidateHttpSession(true)    // 세션 정보 삭제
                .deleteCookies("JSESSIONID")    // 쿠키 삭제 (더 확실하게 로그아웃)
                .permitAll()
        );

        return http.build();
    }
}