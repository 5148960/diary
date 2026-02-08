package com.example.diary1;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    // 사용자의 닉네임(또는 아이디)으로 일기 목록을 필터링해서 가져오는 기능
    List<Diary> findByNickname(String nickname);
}