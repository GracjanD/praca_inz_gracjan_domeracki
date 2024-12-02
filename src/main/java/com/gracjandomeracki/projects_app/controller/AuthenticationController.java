package com.gracjandomeracki.projects_app.controller;

import com.gracjandomeracki.projects_app.entity.User;
import com.gracjandomeracki.projects_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model){
        model.addAttribute("user", new User());
        return "register-panel";
    }

    @GetMapping("/login")
    public String showLoginPage(){
        return "login-panel";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "register-panel";
        }

        if(userService.isDataAvailable(user)){
            userService.registerUser(user);
            return "redirect:/login";
        }

        return "redirect:/register?error";
    }

    @GetMapping("/error")
    public String errorPage(){
        return "error";
    }
}
