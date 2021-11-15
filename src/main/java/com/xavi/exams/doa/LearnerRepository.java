package com.xavi.exams.doa;

import com.xavi.exams.models.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearnerRepository extends JpaRepository <Learner, String> {
}
