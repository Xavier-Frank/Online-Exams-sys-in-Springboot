package com.xavi.exams.controller;


import com.xavi.exams.models.Exams;
import com.xavi.exams.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/exams")
public class ExamsController {
    @Autowired
    private ExamService examService;


    //get examination page
    @GetMapping("/createExams")
    public String examinationPage() {
        return "/examination/create-an-examination";
    }


    //return add new assessment page
    @GetMapping("/createNewAssessment")
    public String createAssessment(Model model){

        model.addAttribute("exams", new Exams());

        return "/examination/createNewAssessment";

    }

    //Add question page
    @GetMapping("/add-questions")
    public String addQuestion(){
        return "/examination/Add-Question";
    }



    //save an exam
    @PostMapping("/createAssessment")
    public ModelAndView createExams(@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                                    Exams exams, ModelAndView modelAndView){
        examService.saveAnExam(exams);
        modelAndView.setViewName("redirect:/api/exams/createExams?success");
        return modelAndView;
    }


}
