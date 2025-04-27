package com.example.E.learning.Platform.Controller;

import com.example.E.learning.Platform.Model.Admin;
import com.example.E.learning.Platform.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public String showLandingPage(){
        return "landing";
    }

    @GetMapping("/register")
    public String openRegisterPage(Model model){
        model.addAttribute("admin", new Admin());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute Admin admin){
       adminService.registerAdmin(admin);
       return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String openLoginPage(){
        return "login";
    }

    @GetMapping("/admin-dashboard")
    public String openWelcomeForm(){
        return "admin-dashboard";
    }
}
