package com.xavi.exams.services;

import com.xavi.exams.doa.*;
import com.xavi.exams.models.Exams;
import com.xavi.exams.models.Instructor;
import com.xavi.exams.models.Learner;
import com.xavi.exams.models.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private LearnerRepository learnerRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ResultsRepository resultsRepository;

    //List of learners
    public List<Learner> getAllLearners(){
        return learnerRepository.findAll();
    }

    //list of instructors
    public List<Instructor> getAllInstructors(){
        return instructorRepository.findAll();
    }

    //List of exams
    public List<Exams> getAllExams(){
        return examRepository.findAll();
    }

    //Results
    public List<Results> getAllResults(){
        return resultsRepository.findAll();
    }

    //CRUD operations

    //Add users




    //Update a user



    //Delete a user


    //Delete an exam

}
