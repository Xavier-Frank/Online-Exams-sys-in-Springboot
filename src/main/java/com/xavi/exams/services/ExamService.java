package com.xavi.exams.services;


import com.xavi.exams.doa.ExamRepository;
import com.xavi.exams.models.Exams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    //Delete exams
    public void deleteById(String assessmentId) throws Exception {
//        String count = examRepository.countById(assessmentId);
//        if (count ==  null){
//            throw new Exception("Assessment not found");
//
//        }
        try {
            examRepository.deleteById(assessmentId);
        } catch (Exception e){
            System.out.println("Error deleting Assessment");
        }
    }


    public Exams get(String assessmentId) throws UserNotFoundException {
        Optional<Exams> getExam = examRepository.findById(assessmentId);

        if (getExam.isPresent()){
            getExam.get();
        }
        throw new UserNotFoundException("Assessment Not Found");
    }

    public void update(Exams exams) {
        examRepository.save(exams);
    }

    public Optional<Exams> findById(String assessmentId) throws UserNotFoundException{
        return examRepository.findById(assessmentId);
    }
}
