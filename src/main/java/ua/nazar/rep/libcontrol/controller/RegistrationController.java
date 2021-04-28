package ua.nazar.rep.libcontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nazar.rep.libcontrol.domain.Role;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.service.UserService;

import java.util.Collections;

@Controller
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            User user,
            Model model) {
        user.setRoles(Collections.singleton(Role.ROLE_CLIENT));
        userService.addUser(user);

        return "redirect:/login";
    }
}
