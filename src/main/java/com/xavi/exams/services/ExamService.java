package com.xavi.exams.services;


import com.xavi.exams.doa.ExamRepository;
import com.xavi.exams.models.Exams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {
    @Autowired
    private ExamRepository examRepository;

    //save an exam
    public Exams saveAnExam(Exams exams){
        return examRepository.save(exams);
    }

    //return exam list
    public List<Exams> assessmentList(){
        return (List<Exams>) examRepository.findAll();
    }



    //search exams
    public List<Exams> searchExams(String keyword){
        return examRepository.searchExam(keyword);
    }



}
