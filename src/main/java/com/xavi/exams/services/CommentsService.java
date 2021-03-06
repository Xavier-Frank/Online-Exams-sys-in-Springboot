package com.xavi.exams.services;

import com.xavi.exams.doa.CommentsRepository;
import com.xavi.exams.models.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;


    //Save a comment
    public Comments saveComment(Comments comments){
        return commentsRepository.save(comments);
    }

    public List<Comments> commentList() {
        return commentsRepository.findAll();
    }

    public void delete(BigInteger id) {
        commentsRepository.deleteById(id);
    }

    public List<Comments> searchcomment(String keyword) {
        return commentsRepository.searchComment(keyword);
    }
}
