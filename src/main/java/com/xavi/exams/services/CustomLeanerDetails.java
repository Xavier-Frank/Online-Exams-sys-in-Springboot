package com.xavi.exams.services;

import com.xavi.exams.models.Learner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Id;
import java.util.Collection;

public class CustomLeanerDetails implements UserDetails {
    private Learner learner;

    public CustomLeanerDetails(Learner learner){
        this.learner=learner;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    //
    @Override
    public String getPassword() {
        return learner.getPassword();
    }

    @Override
    public String getUsername() {
        return learner.getLearnerId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public String getFullName() {
        return learner.getFirstName() + " " + learner.getLastName();
    }

}
