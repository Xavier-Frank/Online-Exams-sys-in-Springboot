package com.xavi.exams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api/main")
public class MainController {

    //Return usertype form
    @GetMapping("/user-type")
    public String userTypeForm(){
        return "index";
    }

    //return help page
    @GetMapping("/help")
    public String helpPage()
    {
        return "online-examination-help";
    }

    @GetMapping("/logout")
    public String userLogout(){
        return "redirect:/api/main/user-type?logoutsuccess";
    }



    //Control User types
    @GetMapping("/recordUserType")
    public String userTypes(@RequestParam("usertype") String usertype){
            if (usertype.equals("instructor")){
                return "redirect:/api/instructor/lec-registrationform";
            }else if (usertype.equals("learner")){
                return "redirect:/api/student/stud-registrationform";
            }else if (usertype.equals("")){
                return "redirect:/api/main/user-type?error";
            }else{
                return "redirect:/api/main/user-type?error";
            }

           }

    //Open the online help feature
    @GetMapping("/online-help")
    public String helpUsers(){
        return "redirect:/api/main/help?success";

    }

}
