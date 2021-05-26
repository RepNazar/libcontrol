package ua.nazar.rep.libcontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nazar.rep.libcontrol.domain.Role;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.service.UserService;

import java.util.regex.Pattern;

@Controller
public class UserController {
    private final static String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private final static Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);

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
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", email);
            model.addAttribute("passwordError", "Passwords are different");
            if (!EMAIL_REGEX.matcher(email).matches()) {
                model.addAttribute("emailError", "Email is not correct");
            }
            return "profile";
        }
        if (!EMAIL_REGEX.matcher(email).matches()) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", email);
            model.addAttribute("emailError", "Email is not correct");
            return "profile";
        }

        userService.updateProfile(user, password, email);
        model.addAttribute("user", null);
        model.addAttribute("passwordError", null);

        return "redirect:/user/profile";
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

    @PreAuthorize("hasAuthority('ROLE_DIRECTOR')")
    @GetMapping("/user/{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ROLE_DIRECTOR')")
    @PostMapping("/users")
    public String saveUser(
            @RequestParam("userId") User user,
            @RequestParam("password") String password,
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("email") String email,
            Model model
    ) {
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("user", user);
            model.addAttribute("passwordError", "Passwords are different");
            if (!EMAIL_REGEX.matcher(email).matches()) {
                model.addAttribute("emailError", "Email is not correct");
            }
            return "userEdit";
        }
        if (!EMAIL_REGEX.matcher(email).matches()) {
            model.addAttribute("user", user);
            model.addAttribute("emailError", "Email is not correct");
            return "userEdit";
        }

        userService.updateProfile(user, password, email);

        model.addAttribute("user", null);
        model.addAttribute("passwordError", null);

        return "redirect:/users";
    }

}