package com.xavi.exams.controller;

import com.xavi.exams.models.*;
import com.xavi.exams.services.LearnerService;
import com.xavi.exams.services.QuestionOpenService;
import com.xavi.exams.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/api/question_and_answer")
public class QuestionAnswerController {

        @Autowired
        Result result;
        @Autowired
        QuizService qService;

        Boolean submitted = false;

        @Autowired
        private LearnerService learnerService;

        @Autowired
        private QuestionOpenService questionOpenService;

        @ModelAttribute("/results")
        public Result getResult() {
            return result;
        }


        @GetMapping("/student-score")
        public String score(Model m) {
            List<Result> sList = qService.getTopScore();
            m.addAttribute("sList", sList);

            return "scoreboard.html";
        }



    //return add question form
    @GetMapping("/add-question")
    public String addQuestionForm(Model model) {
        model.addAttribute("question", new Question());

        return "/examination/Add-Question";
    }

    //return add question form
    @GetMapping("/add-questionE")
    public String addQuestionFormError(Model model) {
//        model.addAttribute("question", new Question());

        return "/examination/Add-Question";
    }


        //Lectures session
        //Adding questions multichoice
        @PostMapping("/save-question-multichoice")
        public String saveQuestionMultiChoice(Question question,
                                   @RequestParam("title") String title,
                                   @RequestParam("optionA") String optionA,
                                   @RequestParam("optionB") String optionB,
                                   @RequestParam("optionC") String optionC,
                                   @RequestParam("optionD") String optionD,
                                   @RequestParam("ans") Integer ans,
                                              @RequestParam("chose") Integer chose) throws Exception {

            if(title.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty()){
                return "redirect:/api/question_and_answer/add-questionE?omission";


            }
            //add question attributes
            chose = -1;
            question.setTitle(title);
            question.setOptionA(optionA);
            question.setOptionB(optionB);
            question.setOptionC(optionC);
            question.setOptionD(optionD);
            question.setAns(ans);
            question.setChose(chose);
            try {
                qService.saveQuestion(question);
                return "redirect:/api/question_and_answer/add-question?success";
            }catch (Exception e){

                return "redirect:/api/question_and_answer/add-questionE?error";
            }

        }

    //Adding questions multichoice
    @PostMapping("/save-question-openend")
    public String saveQuestionOpenEnd(QuestionOpen questionOpen, Integer quesId,
                                      @RequestParam("title") String title){

        if(title.isEmpty()){
            return "redirect:/api/question_and_answer/add-questionE?omission";

        }
        //add question attributes
        questionOpen.setTitle(title);
        try {

            questionOpenService.saveOpenEndQuestion(questionOpen);
            return "redirect:/api/question_and_answer/add-question?success";
        }catch (Exception e){

            return "redirect:/api/question_and_answer/add-questionE?error";
        }

    }

    @GetMapping("/start-assessment")
    public String startAssessment(Model model) throws ParseException {
        QuestionForm qForm = qService.getQuestions();
        model.addAttribute("qForm", qForm);

//        model.addAttribute("timer", new SimpleDateFormat("HH-mm-ss").parse("2022-01-01"));

        model.addAttribute("Hello", "hello goodluck");
        //return the template
        return "/examination/take-assessment";

    }
    @PostMapping("/submit-answers")
    public String submit(@ModelAttribute QuestionForm qForm, Model model) {
        if(!submitted) {
            Integer result1 = qService.getResult(qForm);
            result.setTotalCorrect(qService.getResult(qForm));

            model.addAttribute("result", result1);

            Integer r = result.getTotalCorrect();

            Learner learner = new Learner();
            learner.setResults(r);
//            learnerService.setResults(r);

            qService.saveScore(result);
            submitted = true;
        }

//        return "result.html";

        return "redirect:/api/student/results?success";
    }


    //delete question
    @GetMapping("/delete-question/{quesId}")
    public String deleteExams(@PathVariable("quesId") Integer quesId){
        try{
            qService.deleteById(quesId);
        }catch (Exception e){
            return "redirect:/api/exams/createExams?errorDeleteQ";
        }
        return "redirect:/api/exams/createExams?successDeleteQ";
    }




}
