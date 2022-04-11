package com.xavi.exams.controller;

import com.xavi.exams.models.Comments;
import com.xavi.exams.models.Instructor;
import com.xavi.exams.models.Learner;
import com.xavi.exams.services.CommentsService;
import com.xavi.exams.services.InstructorService;
import com.xavi.exams.services.LearnerService;
import com.xavi.exams.services.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private LearnerService learnerService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private CommentsService commentsService;

    @GetMapping("/page")
    public String showAdminPage(Model model){

        List<Learner> list_of_learners = learnerService.learnerList();
        model.addAttribute("learner", list_of_learners);


        List<Instructor> list_of_instructor = instructorService.instructorList();
        model.addAttribute("instructor", list_of_instructor);


        return "/admin/adminpage";
    }

    @GetMapping("/comments")
    public String showAdminComment(Model model){
        List<Comments> commentsList = commentsService.commentList();
        model.addAttribute("comments", commentsList);

        return "/admin/comment";

    }
    @GetMapping("/search-results")
    public String showR(Model model){


        return "/admin/search-results";

    }
    //delete user
    @PostMapping("/delete-instructor/{staffId}")
    public String deleteLecturer(@PathVariable("staffId") String staffId){
        try{
            instructorService.delete(staffId);
        }catch (Exception e){
            return "redirect:/api/admin/page?error";
        }
        return "redirect:/api/admin/page?successN";
    }
    @GetMapping("/delete-learner/{learnerId}")
    public String deleteLearn(@PathVariable("learnerId") String learnerId){
        try{
            learnerService.delete(learnerId);
        }catch (Exception e){
            return "redirect:/api/admin/page?error";
        }
        return "redirect:/api/admin/page?successN";
    }
    @GetMapping("/delete-comment/{id}")
    public String deleteNotif(@PathVariable("id") BigInteger id){
        try{
            commentsService.delete(id);
        }catch (Exception e){
            return "redirect:/api/admin/page?error";
        }
        return "redirect:/api/admin/page?successN";
    }
    //Search a learner
    @GetMapping("/searchlearner")
    public String searchLearner(Model model, @Param("keyword") String keyword){

        try{
            List<Learner> searchedLearner = learnerService.search(keyword);

            model.addAttribute("keyword", keyword);
            model.addAttribute("searchedLearner", searchedLearner);
            model.addAttribute("pageTitle", "Search Results for: '" + keyword + "'");


        }catch (UserNotFoundException userNotFoundException){
            model.addAttribute("keyword", keyword);
            model.addAttribute("pageTitle", "User not Found");
            model.addAttribute("message", "User does not exist");
            return "/admin/search-results";

        }
        return "/admin/search-results";

    }
    @GetMapping("/searchInstructor")
    public String searchInstructor(Model model, @Param("keyword") String keyword){

        try{
            List<Learner> sInstructor = instructorService.searchInstructor(keyword);
//            List<Learner> searchedInstructor = instructorService.searchInstructor(keyword);

            model.addAttribute("keyword", keyword);
            model.addAttribute("searchedInstructor", sInstructor);
            model.addAttribute("pageTitle", "Search Results for: '" + keyword + "'");


        }catch (UserNotFoundException userNotFoundException){
            model.addAttribute("keyword", keyword);
            model.addAttribute("pageTitle", "User not Found");
            model.addAttribute("message", "User does not exist");
            return "/admin/search-results";

        }
        return "/admin/search-results";

    }
    //Search a comment
    @GetMapping("/searchNotification")
    public String searchComment(@Param("keyword") String keyword, Model model){
        List<Comments> searchedcomment = commentsService.searchcomment(keyword);


        model.addAttribute("searchedNotification", searchedcomment);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Search Results for: '" + keyword + "'");


        return "/admin/search-results";
    }


}
