package com.xavi.exams.controller;


import com.xavi.exams.models.Exams;
import com.xavi.exams.models.Notifications;
import com.xavi.exams.models.Question;
import com.xavi.exams.models.QuestionForm;
import com.xavi.exams.services.ExamService;
import com.xavi.exams.services.QuizService;
import com.xavi.exams.services.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/exams")
public class ExamsController {
    @Autowired
    private ExamService examService;

    @Autowired
    private QuizService qService;
    @Autowired
    private QuestionForm questionForm;


    //get examination page and display assessment details
    @GetMapping("/createExams")
    public String examinationPage(Model model) {
        List<Exams> examsList = examService.assessmentList();
        model.addAttribute("exams", examsList);

        QuestionForm qForm = qService.getQuestions();
        model.addAttribute("qForm", qForm);

        //return the template
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
    public String addQuestion(Model model){
        model.addAttribute("question", new Question());
        return "/examination/Add-Question";
    }



    //save an exam
    @PostMapping("/createAssessment")
    public ModelAndView createExams(Exams exams, ModelAndView modelAndView, @RequestParam("staffId") String staffId){
        exams.setStaffId(staffId);
        examService.saveAnExam(exams);
        modelAndView.setViewName("redirect:/api/exams/createExams?success");
        return modelAndView;
    }

    //Delete exams

    @GetMapping("/delete-exam/{assessmentId}")
    public String deleteExams(@PathVariable("assessmentId") String assessmentId){
        try{
            examService.deleteById(assessmentId);
        }catch (Exception e){
            return "redirect:/api/exams/createExams?error";
        }
        return "redirect:/api/exams/createExams?successDelete";
    }

    //edit exams
    @GetMapping("/edit-exam/{assessmentId}")
    public String editExams(@PathVariable("assessmentId") String assessmentId, Model model){
        try {
            Exams exams = examService.get(assessmentId);
            model.addAttribute("exams", exams);
            model.addAttribute("pageTitle", "Update Assessment");
            model.addAttribute("formHeader", "Edit Assessment: " + "Assessment ID" + "(" + assessmentId + ")" );

        }catch (UserNotFoundException e){
            return "redirect:/api/exams/createExams?erroredit";

        }

        return "examination/registerAssessment";

    }
    //edit an exam
    @PostMapping("/editAssessment")
    public ModelAndView editExams(Exams exams, ModelAndView modelAndView){
        examService.saveAnExam(exams);
        modelAndView.setViewName("redirect:/api/exams/createExams?successedit");
        return modelAndView;
    }



}
