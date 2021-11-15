package com.xavi.exams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/student")
public class LearnerController {

    //return the login page
    @GetMapping("/stud-loginform")
    public String studentLoginForm(){
        return "stud-loginform";
    }

    //return registration form
    @GetMapping("/stud-registrationform")
    public String studentRegistrationForm(){
        return "stud-registration";
    }




}
