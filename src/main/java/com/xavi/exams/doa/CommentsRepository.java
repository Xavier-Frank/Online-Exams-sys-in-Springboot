package com.xavi.exams.doa;

import com.xavi.exams.models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, BigInteger> {
    public Long countById(BigInteger id);


    @Query(value = "SELECT * FROM comments WHERE" + " "
            + "MATCH (content)" + " " + "AGAINST(?1)", nativeQuery = true)
    List<Comments> searchComment(String keyword);
}
