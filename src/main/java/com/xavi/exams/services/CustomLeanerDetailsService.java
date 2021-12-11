package com.xavi.exams.services;

import com.xavi.exams.doa.LearnerRepository;
import com.xavi.exams.models.Learner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomLeanerDetailsService implements UserDetailsService {
    @Autowired
    private LearnerRepository learnerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Learner learner = learnerRepository.findByLearnerId(username);

        if (learner == null ){
            throw new UsernameNotFoundException("User Does not Exist in the Database");
        }
        return new CustomLeanerDetails(learner);
    }
}
