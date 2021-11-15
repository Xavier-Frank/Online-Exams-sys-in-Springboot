package com.xavi.exams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/main")
public class MainController {

    //Return usertype form
    @GetMapping("/user-type")
    public String userTypeForm(){
        return "index";
    }

    //Control User types
    @GetMapping("/recordUserType")
    public String userTypes(@RequestParam("") String usertype){
            if (usertype.equals("instructor")){
                return "redirect:/api/instructor/lec-loginform";
            }else if (usertype.equals("learner")){
                return "redirect:/api/student/stud-loginform";
            }else if (usertype.equals("")){
                return "redirect:/user-type?error";
            }else{
                return "redirect:/user-type?error";
            }

           }

}
