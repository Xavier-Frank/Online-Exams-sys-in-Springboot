package com.xavi.exams.services;

import com.xavi.exams.doa.NotificationRepository;
import com.xavi.exams.models.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
}
