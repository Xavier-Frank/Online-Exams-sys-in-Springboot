package com.xavi.exams.services;

import com.xavi.exams.doa.InstructorRepository;
import com.xavi.exams.doa.ResultsRepository;
import com.xavi.exams.models.Instructor;
import com.xavi.exams.models.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.instrument.Instrumentation;
import java.util.List;

@Service
public class InstructorService {
    @Autowired
    private ResultsRepository resultsRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    //register an instructor
    public Instructor registerInstructor(Instructor instructor){
        return instructorRepository.save(instructor);

    }
    //Forgot Password functionality
    public void updateResetPasswordToken(String token, String email) throws ClassNotFoundException {
        //get the existing email
        Instructor instructor = instructorRepository.findByEmail(email);
        if (instructor != null){
            instructor.setResetPasswordToken(token);
            instructorRepository.save(instructor);
        }else {
            throw new ClassNotFoundException("No user with such email exist" + email);
        }

    }
    public Instructor getByResetPasswordToken(String token){
        return instructorRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(Instructor instructor, String newPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(newPassword);
        instructor.setPassword(encodedPassword);

        //set tokento null so that it cannot be used again
        instructor.setResetPasswordToken(null);
        instructorRepository.save(instructor);
    }


    //login


    //View results
    public List<Results> getAllResults(){
        return resultsRepository.findAll();
    }

    //Create exams





}
