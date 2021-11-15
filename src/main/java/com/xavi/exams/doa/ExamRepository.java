package com.xavi.exams.doa;

import com.xavi.exams.models.Exams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository <Exams, String> {
}
