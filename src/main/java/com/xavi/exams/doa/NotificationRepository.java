package com.xavi.exams.doa;

import com.xavi.exams.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, BigInteger> {

    public Long countById(BigInteger id);

    @Query(value = "SELECT * FROM notifications WHERE" + " "
            + "MATCH (content)" + " " + "AGAINST(?1)", nativeQuery = true)
    public List<Notifications> searchNotification(String keyword);


//    @Query("SELECT id, content, createdOn FROM Notifications n WHERE n.id")
//    Notifications findNotificationById(BigInteger id);
}
