package com.xavi.exams.doa;

import com.xavi.exams.models.Exams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository <Exams, String> {


    @Query(value = "SELECT * FROM exams WHERE" + " "
            + "MATCH (assessment_id, assessment_name, department, staff_id)" + " "
            + "AGAINST (?1)", nativeQuery = true)
    public List<Exams> searchExam(String keyword);


}
