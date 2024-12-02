package com.gracjandomeracki.projects_app.controller;

import com.gracjandomeracki.projects_app.DTO.UserUpdateDTO;
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
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/account")
    public String showAccountPage(Model model){
        User user = userService.getCurrentUser();
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(user.getFirstName(),user.getLastName());

        model.addAttribute("user", user);
        model.addAttribute("userUpdateDTO", userUpdateDTO);
        return "account-panel";
    }

    @PostMapping("/account/updateAccount")
    public String updateUser(@Valid @ModelAttribute("userUpdateDTO") UserUpdateDTO userUpdateDTO,
                           BindingResult bindingResult, Model model){
        User user = userService.getCurrentUser();
        if(bindingResult.hasErrors()){
            model.addAttribute("user", user);
            return "account-panel";
        }

        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        userService.save(user);
        return "redirect:/account?success";

    }
}
