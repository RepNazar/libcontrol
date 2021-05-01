package ua.nazar.rep.libcontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nazar.rep.libcontrol.domain.Role;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.service.UserService;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", email);
            model.addAttribute("passwordError", "Passwords are different");
            return "profile";
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }

        return "loginPage";
    }

}
