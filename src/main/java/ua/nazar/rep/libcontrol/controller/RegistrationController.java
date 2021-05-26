package ua.nazar.rep.libcontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.nazar.rep.libcontrol.domain.Role;
import ua.nazar.rep.libcontrol.domain.User;
import ua.nazar.rep.libcontrol.service.UserService;

import javax.validation.Valid;
import java.util.Map;

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
    public String addClient(
            @RequestParam("password2") String passwordConfirm,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {
        if (addUserFailed(Role.ROLE_CLIENT, passwordConfirm, user, bindingResult, model)) {
            return "registration";
        }
        return "redirect:/login";
    }

    @PreAuthorize("hasAuthority('ROLE_DIRECTOR')")
    @GetMapping("/register")
    public String getRoles(Model model) {
        model.addAttribute("roles", Role.values());
        return "register";
    }

    @PreAuthorize("hasAuthority('ROLE_DIRECTOR')")
    @PostMapping("/register")
    public String addEmployee(
            @RequestParam("role") Role role,
            @RequestParam("password2") String passwordConfirm,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {
        if (addUserFailed(role, passwordConfirm, user, bindingResult, model)) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("user", user);
            return "register";
        }
        return "redirect:/users";
    }

    private boolean addUserFailed(Role role, String passwordConfirm, @Valid User user, BindingResult bindingResult, Model model) {
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        boolean isPasswordsDifferent = user.getPassword() != null &&
                !user.getPassword().equals(passwordConfirm);

        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password Confirmation cannot be empty");
        }
        if (isPasswordsDifferent) {
            model.addAttribute("passwordError", "Passwords are different");
        }
        if (isConfirmEmpty || isPasswordsDifferent || bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return true;
        }
        if (!userService.addUser(user, role)) {
            model.addAttribute("usernameError", "User exists!");
            return true;
        }

        return false;
    }
}
