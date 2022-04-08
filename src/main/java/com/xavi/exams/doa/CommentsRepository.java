package com.xavi.exams.doa;

import com.xavi.exams.models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, BigInteger> {
    public Long countById(BigInteger id);
}
