package com.xavi.exams.services;

import com.xavi.exams.doa.ResultsRepository;
import com.xavi.exams.models.Results;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InstructorService {
    @Autowired
    private ResultsRepository resultsRepository;

    //register



    //login


    //View results
    public List<Results> getAllResults(){
        return resultsRepository.findAll();
    }

    //Create exams





}
