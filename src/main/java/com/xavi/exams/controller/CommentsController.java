package com.xavi.exams.controller;

import com.xavi.exams.models.Comments;
import com.xavi.exams.services.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping("/save-comments")
    public String saveComment(@RequestParam("content") String content, Comments comments){
        content.toString();

        comments.setContent(content);
        commentsService.saveComment(comments);

        return "redirect:/api/main/help?saveSuccess";

    }

}
