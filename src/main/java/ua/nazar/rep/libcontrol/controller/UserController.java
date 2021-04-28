package ua.nazar.rep.libcontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addClient(
            User user,
            Model model) {
        user.setRoles(Collections.singleton(Role.ROLE_CLIENT));
        userService.addUser(user);

        return "redirect:/login";
    }

    @PreAuthorize("hasAuthority('ROLE_DIRECTOR')")
    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAllNotClient());

        return "userList";
    }

    @GetMapping("/user/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        User userFromDb = userService.findUser(user);
        model.addAttribute("username", userFromDb.getUsername());
        model.addAttribute("email", userFromDb.getEmail());

        return "profile";
    }
    @PostMapping("/user/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam("password2") String passwordConfirm,
            Model model
    ) {
        if (password.equals(passwordConfirm)) {

            userService.updateProfile(user, password, email);
            model.addAttribute("user", null);
            model.addAttribute("passwordError", null);

            return "redirect:/user/profile";
        } else {
            return "profile";
        }
    }

    @GetMapping("/register")
    public String addEmployee(Model model){
        model.addAttribute("roles",Role.values());
        return "register";
    }

    @PostMapping("/register")
    public String addEmployee(
            @RequestParam("role") Role role,
            User user,
            Model model) {
        user.setRoles(Collections.singleton(role));
        userService.addUser(user);

        return "redirect:/register";
    }
}
