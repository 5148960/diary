package com.example.diary1;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity @Getter @Setter
public class Diary {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String fileName;

    // ⭐ 이 한 줄만 추가하면 됩니다!
    // @Getter/@Setter 덕분에 자동으로 인식돼서 빨간 줄이 사라질 거예요.
    private String nickname;

    private LocalDateTime createdAt = LocalDateTime.now();
}