package com.xavi.exams.services;

import com.xavi.exams.doa.LearnerRepository;
import com.xavi.exams.models.Instructor;
import com.xavi.exams.models.Learner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearnerService {
    @Autowired
    private LearnerRepository learnerRepository;

    //register a leaner
    public Learner registerLeaner(Learner learner){
        return learnerRepository.save(learner);

    }

    //forgot password functionality
    public void updateResetPasswordToken(String token, String email) throws ClassNotFoundException {
        //get the existing email
        Learner learner = learnerRepository.findByEmail(email);

        if (learner != null){
            learner.setResetPasswordToken(token);
            learnerRepository.save(learner);
        }else {
            throw new ClassNotFoundException("No user with such email exist" + email);
        }
    }

    public Learner getByResetPasswordToken(String token){
        return learnerRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(Learner learner, String newPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(newPassword);
        learner.setPassword(encodedPassword);

        learner.setResetPasswordToken(null);
        learnerRepository.save(learner);
    }

    // list of learners
    public List<Learner> learnerList(){
        return (List<Learner>) learnerRepository.findAll();
    }
    //View results

    //Take Assessment

    //contact lecturer

}
