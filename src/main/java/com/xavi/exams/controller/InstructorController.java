package com.xavi.exams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/instructor")
public class InstructorController {
    //return the login page
    @GetMapping("/lec-loginform")
    public String instructorLoginForm(){
        return "lec-loginform";
    }

    //return registration form
    @GetMapping("/lec-registrationform")
    public String intructorRegistrationForm(){
        return "lec-registration";
    }

}
