package kr.ac.hansung.cse.hellospringdatajpa.controller;

import kr.ac.hansung.cse.hellospringdatajpa.entity.MyRole;
import kr.ac.hansung.cse.hellospringdatajpa.entity.MyUser;
import kr.ac.hansung.cse.hellospringdatajpa.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new MyUser());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute MyUser user, Model model) {

        // 1) 이메일 중복 체크
        if (registrationService.checkEmailExists(user.getEmail())) {
            model.addAttribute("emailExists", true);
            return "signup";
        }

        // 2) 권한 결정
        List<MyRole> userRoles = new ArrayList<>();

        // admin@ 로 시작하면 관리자, 그 외는 일반 사용자
        if (user.getEmail() != null && user.getEmail().toLowerCase().startsWith("admin@")) {
            userRoles.add(registrationService.findByRolename("ROLE_ADMIN"));
        } else {
            userRoles.add(registrationService.findByRolename("ROLE_USER"));
        }

        // 3) 회원 생성
        registrationService.createUser(user, userRoles);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
