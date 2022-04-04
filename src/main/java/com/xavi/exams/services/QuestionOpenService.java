package com.xavi.exams.services;

import com.xavi.exams.doa.QuestionOpenRepo;
import com.xavi.exams.models.Question;
import com.xavi.exams.models.QuestionOpen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionOpenService {
    @Autowired
    private QuestionOpenRepo questionOpenRepo;

    //save open end question
    public QuestionOpen saveOpenEndQuestion(QuestionOpen questionOpen) throws Exception {

//        Optional<QuestionOpen> questionId = questionOpenRepo.findById(quesId);
//        if (questionId.isPresent()) {
//
//            throw new Exception("Question Id already exists");
//        } else {

            return questionOpenRepo.save(questionOpen);
//        }
    }
}
