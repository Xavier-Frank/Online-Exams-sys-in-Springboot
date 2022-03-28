package com.xavi.exams.doa;

import com.xavi.exams.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, BigInteger> {

    public Long countById(BigInteger id);

    @Query(value = "SELECT * FROM notifications WHERE" + " "
            + "MATCH (content)" + " " + "AGAINST(?1)", nativeQuery = true)
    public List<Notifications> searchNotification(String keyword);



}
