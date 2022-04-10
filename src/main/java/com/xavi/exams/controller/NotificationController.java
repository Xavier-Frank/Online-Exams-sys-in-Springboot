package com.xavi.exams.controller;

import com.xavi.exams.models.Notifications;
import com.xavi.exams.services.NotificationService;
import com.xavi.exams.services.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

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
//    @GetMapping("/edit-notification/{id}")
//    public String editNotification(@PathVariable("id") BigInteger id, Model model) throws UserNotFoundException {
//        System.out.println("The notification id to delete is" + ":" + id);
//        try{
//            Notifications notifications = notificationService.editNotification(id);
//            model.addAttribute("pageTitle", "Update Notifications");
//            model.addAttribute("formHeader", "Edit Notification: " + "Notification ID" + "(" + id + ")" );
//        } catch (UserNotFoundException e){
//            return "redirect:/api/instructor/announcements?erroredit";
//        }
//        return "/instructor/lec-notification-edit";
//    }
//
//    //edit notification
//    @PostMapping("/edit-notification")
//    public String editNotification(Notifications notifications, @RequestParam("content") String content){
//        content.toString();
//        notifications.setContent(content);
//        notificationService.saveNotification(notifications);
//
//        return "redirect:/api/notifications/instructor-notification?successedit";
//    }
    //edit notification
    @GetMapping(value = {"/{id}/edit"})
    public String showEditNotification(Model model, @PathVariable BigInteger id) {
       Optional<Notifications> notifications = null;
        try {
            notificationService.findById(id).ifPresent(o -> model.addAttribute("notifications", o) );
//            System.out.println("the retrieved notification id is: " + );
            model.addAttribute("pageTitle", "Edit a notification");
//            model.addAttribute("notifications", notifications);

        } catch (UserNotFoundException ex) {
            model.addAttribute("errorMessage", "Notification not found");
            return "/instructor/lec-notification";
        }
        return "/instructor/lec-notification-edit";
    }

    //update notification
    @PostMapping(value = {"/{id}/edit"})
    public String updateContact(Model model,
                                @RequestParam("content") String content,
                                @ModelAttribute("notifications") Notifications notifications) {
        try {
            notifications.setContent(content);
            notificationService.update(notifications);
            return "redirect:/api/notifications/instructor-notification?successedit";
        } catch (Exception ex) {
            // log exception first,
            // then show error
//            String errorMessage = ex.getMessage();
//            logger.error(errorMessage);
            model.addAttribute("errorMessage", "Error editing notification");

            return "/instructor/lec-notification-edit";
        }
    }

}
