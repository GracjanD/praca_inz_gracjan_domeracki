package com.gracjandomeracki.projects_app.controller;

import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.Report;
import com.gracjandomeracki.projects_app.entity.Task;
import com.gracjandomeracki.projects_app.entity.User;
import com.gracjandomeracki.projects_app.service.ReportService;
import com.gracjandomeracki.projects_app.service.TaskService;
import com.gracjandomeracki.projects_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class ReportController {

    private ReportService reportService;
    private TaskService taskService;
    private UserService userService;

    @Autowired
    public ReportController(ReportService reportService, TaskService taskService, UserService userService){
        this.reportService = reportService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping("/tasks/{taskId}/createReport")
    public String createReport(@PathVariable int taskId, @ModelAttribute("report") Report report){
        Task task = taskService.findById(taskId);
        User currentUser = userService.getCurrentUser();

        if(!task.hasUser(currentUser)){
            return "error";
        }

        report.setTask(task);
        report.setAuthor(currentUser);
        report.setCreationDate(LocalDate.now());
        reportService.save(report);

        return "redirect:/tasks/" + taskId;
    }

    @PostMapping("/tasks/{taskId}/deleteReport")
    public String deleteReport(@PathVariable int taskId, @RequestParam int reportId){
        Task task = taskService.findById(taskId);
        User currentUser = userService.getCurrentUser();
        Project project = task.getProject();
        Report report = reportService.findById(reportId);

        if(!project.isLeader(currentUser)){
            if(!task.hasUser(currentUser)){
                return "error";
            } else {
                if(!report.isAuthor(currentUser)){
                    return "error";
                }
            }
        }

        reportService.deleteById(reportId);

        return "redirect:/tasks/" + taskId;
    }
}
