package com.xavi.exams.controller;

import com.xavi.exams.models.Question;
import com.xavi.exams.models.QuestionForm;
import com.xavi.exams.models.QuestionOpen;
import com.xavi.exams.models.Result;
import com.xavi.exams.services.QuestionOpenService;
import com.xavi.exams.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        private QuestionOpenService questionOpenService;

        @ModelAttribute("/results")
        public Result getResult() {
            return result;
        }

//        @GetMapping("/")
//        public String home() {
//            return "index.html";
//        }


        @PostMapping("/start-quiz")
        public String quiz(@RequestParam String username, Model m, RedirectAttributes ra) {
//            if(username.equals("")) {
//                ra.addFlashAttribute("warning", "You must enter your name");
//                return "redirect:/";
//            }

            submitted = false;

            result.setUsername(username);

            QuestionForm qForm = (QuestionForm) qService.getQuestions();
            m.addAttribute("qForm", qForm);

            return "quiz.html";
        }

        @PostMapping("/submit-answers")
        public String submit(@ModelAttribute QuestionForm qForm, Model m) {
            if(!submitted) {
                result.setTotalCorrect(qService.getResult(qForm));
                qService.saveScore(result);
                submitted = true;
            }

            return "result.html";
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
                                   @RequestParam("ans") Integer ans) throws Exception {

            if(title.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty()){
                return "redirect:/api/question_and_answer/add-questionE?omission";


            }
            //add question attributes
            question.setTitle(title);
            question.setOptionA(optionA);
            question.setOptionB(optionB);
            question.setOptionC(optionC);
            question.setOptionD(optionD);
            question.setAns(ans);
            try {
                qService.saveQuestion(question);
                System.out.println("Hello world");
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


}
