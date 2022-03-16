package com.xavi.exams.services;

import com.xavi.exams.doa.NotificationRepository;
import com.xavi.exams.models.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class NotificationService {
    @Autowired private NotificationRepository notificationRepository;


    //save notification
    public Notifications saveNotification(Notifications notifications){

        return notificationRepository.save(notifications);

    }

    //list of notifications

    public List<Notifications> notificationsList(){
        return (List<Notifications>) notificationRepository.findAll();
    }

    //delete a notification
    public void delete(BigInteger id) throws Exception {
        Long count = notificationRepository.countById(id);
        if (count ==  null || count == 0){
            throw new Exception("Notification not found");

        }
        notificationRepository.deleteById(id);

    }
}
