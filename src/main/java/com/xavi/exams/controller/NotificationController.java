package com.xavi.exams.controller;

import com.xavi.exams.models.Notifications;
import com.xavi.exams.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.http.HttpRequest;
import java.util.List;

@Controller
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired private NotificationService notificationService;

    //save notifications
    @PostMapping("/save-notification")
    public String saveNotification(Notifications notifications, @RequestParam("content") String content){
        content.toString();
//        if (content.chars().equals("<p>") || content.chars().equals("</p>") || content.chars().equals("<b>") || content.chars().equals("</b>")){
//
//        }
        notifications.setContent(content);
        notificationService.saveNotification(notifications);

        return "redirect:/api/notifications/instructor-notification?success";
    }


    // display notifications
    @GetMapping("/instructor-notification")
    public String instructorNotification(Model model){

        List<Notifications> notificationsList = notificationService.notificationsList();
        model.addAttribute("notifications", notificationsList);
//        model.addAttribute("notifications", new Notifications());

        //return the notification template
        return "/instructor/lec-notification";
    }

    //delete notification
    @GetMapping("/delete-notification/{id}")
     public String deleteNotification(@PathVariable("id")BigInteger id){
        try{
            notificationService.delete(id);
        }catch (Exception e){
            return "redirect:/api/instructor/announcements?error";
        }
        return "redirect:/api/instructor/announcements?successN";
    }

}
