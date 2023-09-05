package org.example.controllers;

import org.example.models.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("home", "Main page");
        return "home";
    }

    @GetMapping("/user")
    public String userPage(Model model){
        model.addAttribute("user", "User page");
        return "user";
    }

    @GetMapping("/admin")
    public String adminPage(Model model){
        model.addAttribute("admin", "Admin page");
        return "admin";
    }

    @GetMapping("/authenticated")
    public String authenticatedPage(Principal principal){
        User user = userService.findByUsername(principal.getName());
        System.out.println(user.getUsername() +" "+ user.getAuthorities());
        return "authenticated";
    }

}
