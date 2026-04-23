package com.mycompany.labkic_3.controller;

import com.mycompany.labkic_3.dto.RegistrationRequest;
import com.mycompany.labkic_3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "registration";
    }

    @PostMapping("/registration")
    public String register(@Valid @ModelAttribute RegistrationRequest registrationRequest,
                           BindingResult bindingResult,
                           Model model) {
        if (userService.usernameExists(registrationRequest.getUsername())) {
            bindingResult.rejectValue("username", "exists", "Username already exists");
        }

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.register(registrationRequest.getUsername(), registrationRequest.getPassword());
        model.addAttribute("successMessage", "Registration successful. Please sign in.");
        return "login";
    }
}
