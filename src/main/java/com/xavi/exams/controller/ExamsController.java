package com.xavi.exams.controller;


import com.xavi.exams.models.Exams;
import com.xavi.exams.models.Question;
import com.xavi.exams.models.QuestionForm;
import com.xavi.exams.services.ExamService;
import com.xavi.exams.services.QuizService;
import com.xavi.exams.services.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

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

    //edit assessments
    @GetMapping(value = {"/{assessmentId}/edit"})
    public String showEditAssessmentForm(Model model, @PathVariable String assessmentId) {
        Optional<Exams> exams = null;
        try {
            examService.findById(assessmentId).ifPresent(o -> model.addAttribute("exams", o) );
//            System.out.println("the retrieved notification id is: " + );
            model.addAttribute("pageTitle", "Edit an Assessment");
            model.addAttribute("formHeader", "Update details");
//            model.addAttribute("notifications", notifications);

        } catch (UserNotFoundException ex) {
            model.addAttribute("errorMessage", "Assessment not found");
            return "/examination/create-an-examination";
        }
        return "/examination/registerAssessment";
    }

    //update notification
    @PostMapping(value = {"/{assessmentId}/edit"})
    public String updateContact(Model model,
                                @PathVariable String assessmentId,
                                @ModelAttribute("exams") Exams exams) {
        try {
            exams.setAssessmentId(assessmentId);
            examService.update(exams);
            return "redirect:/api/exams/createExams?successedit";
        } catch (Exception ex) {
            // log exception first,
            // then show error
//            String errorMessage = ex.getMessage();
//            logger.error(errorMessage);
            model.addAttribute("errorMessage", "Error editing notification");

            return "/examination/create-an-examination";
        }
    }






}
