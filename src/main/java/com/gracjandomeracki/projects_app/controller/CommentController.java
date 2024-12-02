package com.gracjandomeracki.projects_app.controller;

import com.gracjandomeracki.projects_app.entity.*;
import com.gracjandomeracki.projects_app.service.CommentService;
import com.gracjandomeracki.projects_app.service.ReportService;
import com.gracjandomeracki.projects_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class CommentController {
    private CommentService commentService;
    private ReportService reportService;
    private UserService userService;

    @Autowired
    public CommentController(CommentService commentService, ReportService reportService, UserService userService){
        this.commentService = commentService;
        this.reportService = reportService;
        this.userService = userService;
    }

    @PostMapping("/tasks/{taskId}/reports/{reportId}/createComment")
    public String createComment(@PathVariable int taskId, @PathVariable int reportId, @ModelAttribute("comment") Comment comment){
        Report report = reportService.findById(reportId);
        User currentUser = userService.getCurrentUser();
        Task task = report.getTask();
        Project project = task.getProject();


        if(!project.isLeader(currentUser)) {
            if (!project.hasUser(currentUser)) {
                return "error";
            } else {
                if (!project.isTaskVisibility() && !task.hasUser(currentUser)) {
                    return "error";
                }
            }
        }

        comment.setReport(report);
        comment.setAuthor(currentUser);
        comment.setCreationDate(LocalDate.now());

        commentService.save(comment);

        return "redirect:/tasks/" + taskId;
    }

    @PostMapping("/tasks/{taskId}/reports/{reportId}/deleteComment")
    public String deleteComment(@PathVariable int taskId, @PathVariable int reportId, @RequestParam int commentId){
        Report report = reportService.findById(reportId);
        User currentUser = userService.getCurrentUser();
        Task task = report.getTask();
        Project project = task.getProject();
        Comment comment = commentService.findById(commentId);

        if(!project.isLeader(currentUser)) {
            if (!project.hasUser(currentUser)) {
                return "error";
            } else {
                if (!project.isTaskVisibility() && !task.hasUser(currentUser)) {
                    return "error";
                } else {
                    if(!comment.isAuthor(currentUser)){
                        return "error";
                    }
                }
            }
        }

        commentService.deleteById(commentId);

        return "redirect:/tasks/" + taskId;
    }

}
