package com.xavi.exams.controller;

import com.xavi.exams.models.Comments;
import com.xavi.exams.models.Instructor;
import com.xavi.exams.models.Learner;
import com.xavi.exams.services.CommentsService;
import com.xavi.exams.services.InstructorService;
import com.xavi.exams.services.LearnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
