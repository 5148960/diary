package com.example.diary1;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Getter @Setter
@Table(name = "users") // <-- 이 줄을 반드시 추가해야 'user' 예약어 에러가 안 납니다!
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
}