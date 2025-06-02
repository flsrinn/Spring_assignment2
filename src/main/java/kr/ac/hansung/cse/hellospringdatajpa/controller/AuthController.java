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
        // 이메일 중복 체크 (선택)
        if (registrationService.checkEmailExists(user.getEmail())) {
            model.addAttribute("emailExists", true);
            return "signup";
        }

        // 기본 권한 리스트 생성
        List<MyRole> userRoles = new ArrayList<>();
        userRoles.add(registrationService.findByRolename("ROLE_USER"));

        registrationService.createUser(user, userRoles);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
