package com.example.diary1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.security.Principal; // 추가됨
import java.util.UUID;

@Controller
public class DiaryController {

    @Autowired private DiaryRepository diaryRepository;
    @Autowired private GuestbookRepository guestbookRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private final String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            // ⭐ 핵심: 전체 조회가 아니라, 로그인한 사용자의 닉네임으로 쓴 글만 가져옴
            model.addAttribute("list", diaryRepository.findByNickname(principal.getName()));
        }
        model.addAttribute("guests", guestbookRepository.findAll());
        return "index";
    }

    @GetMapping("/join") public String joinForm() { return "join"; }
    @PostMapping("/join")
    public String join(User user) {
        userRepository.save(user);
        return "redirect:/login";
    }
    @GetMapping("/login") public String loginForm() { return "login"; }

    @PostMapping("/save")
    public String save(@RequestParam(required = false) Long id,
                       @RequestParam String title,
                       @RequestParam String content,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       Principal principal) throws IOException { // Principal 추가

        Diary diary = (id != null) ? diaryRepository.findById(id).orElse(new Diary()) : new Diary();
        diary.setTitle(title);
        diary.setContent(content);

        // ⭐ 핵심: 일기를 저장할 때 "누가 썼는지(로그인한 아이디)"를 함께 저장
        if (principal != null) {
            diary.setNickname(principal.getName());
        }

        if (file != null && !file.isEmpty()) {
            File dir = new File(uploadPath);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            file.transferTo(new File(dir, fileName));
            diary.setFileName(fileName);
        } else if (id != null) {
            diaryRepository.findById(id).ifPresent(old -> diary.setFileName(old.getFileName()));
        }

        diaryRepository.save(diary);
        return "redirect:/";
    }

    @GetMapping("/diary/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("diary", diaryRepository.findById(id).orElse(null));
        return "detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteDiary(@PathVariable Long id) {
        diaryRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("diary", diaryRepository.findById(id).orElse(null));
        return "edit";
    }

    @PostMapping("/guest/save")
    public String saveGuest(@RequestParam String nickname, @RequestParam String message) {
        Guestbook guest = new Guestbook();
        guest.setNickname(nickname);
        guest.setMessage(message);
        guestbookRepository.save(guest);
        return "redirect:/";
    }
}