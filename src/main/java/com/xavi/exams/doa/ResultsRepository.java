package com.xavi.exams.doa;

import com.xavi.exams.models.Results;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultsRepository extends JpaRepository<Results, Long> {


}
