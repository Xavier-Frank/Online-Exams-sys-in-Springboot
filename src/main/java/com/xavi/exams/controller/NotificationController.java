package com.xavi.exams.controller;

import com.xavi.exams.models.Exams;
import com.xavi.exams.models.Notifications;
import com.xavi.exams.services.NotificationService;
import com.xavi.exams.services.UserNotFoundException;
import org.aspectj.weaver.ast.Not;
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
     public String deleteNotification(@PathVariable("id") BigInteger id){
        try{
            notificationService.delete(id);
        }catch (Exception e){
            return "redirect:/api/instructor/announcements?error";
        }
        return "redirect:/api/instructor/announcements?successN";
    }

    //edit Notification
    @GetMapping("/edit-notification/{id}")
    public String editNotification(@PathVariable("id") BigInteger id, Model model) throws UserNotFoundException {
        System.out.println("The notification id to delete is" + ":" + id);
        try{
            Notifications notifications = notificationService.editNotification(id);
            System.out.println("Notifications id " + notifications.getId());
            System.out.println("Notifications content " + notifications.getContent());
            System.out.println("Notifications created on " + notifications.getCreatedOn());
            model.addAttribute("notifications", notifications);
            model.addAttribute("pageTitle", "Update Notifications");
            model.addAttribute("formHeader", "Edit Notification: " + "Notification ID" + "(" + id + ")" );
        } catch (UserNotFoundException e){
            return "redirect:/api/instructor/announcements?erroredit";
        }
        return "/instructor/lec-notification-edit";
    }

    //edit notification
    @PostMapping("/edit-notification")
    public String editNotification(Notifications notifications, @RequestParam("content") String content){
        content.toString();
        notifications.setContent(content);
        notificationService.saveNotification(notifications);

        return "redirect:/api/notifications/instructor-notification?successedit";
    }


}
