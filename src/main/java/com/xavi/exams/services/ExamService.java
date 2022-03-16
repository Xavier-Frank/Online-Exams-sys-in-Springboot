package com.xavi.exams.services;


import com.xavi.exams.doa.ExamRepository;
import com.xavi.exams.models.Exams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamService {
    @Autowired
    private ExamRepository examRepository;

    //save an exam
    public Exams saveAnExam(Exams exams){
        return examRepository.save(exams);
    }






}
