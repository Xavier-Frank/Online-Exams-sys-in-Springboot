package com.xavi.exams.services;

import com.xavi.exams.doa.CommentsRepository;
import com.xavi.exams.models.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;


    //Save a comment
    public Comments saveComment(Comments comments){
        return commentsRepository.save(comments);
    }
}
