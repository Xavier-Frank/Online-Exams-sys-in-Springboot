package com.xavi.exams.doa;

import com.xavi.exams.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, BigInteger> {

    public Long countById(BigInteger id);
}
