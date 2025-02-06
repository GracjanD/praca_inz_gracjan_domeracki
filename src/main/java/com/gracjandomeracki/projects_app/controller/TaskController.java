package com.gracjandomeracki.projects_app.controller;

import com.gracjandomeracki.projects_app.entity.*;
import com.gracjandomeracki.projects_app.service.ProjectService;
import com.gracjandomeracki.projects_app.service.TaskService;
import com.gracjandomeracki.projects_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TaskController {

    private TaskService taskService;
    private UserService userService;
    private ProjectService projectService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, ProjectService projectService){
        this.taskService = taskService;
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId, Model model){
        User currentUser = userService.getCurrentUser();
        Project project = projectService.findById(projectId);

        if(!project.isLeader(currentUser)){
            return "error";
        }

        model.addAttribute("task", new Task());
        model.addAttribute("user", currentUser);
        model.addAttribute("projectId", projectId);
        return "task-create-panel";
    }

    @PostMapping("/projects/{projectId}/saveTask")
    public String saveTask(@PathVariable int projectId, @ModelAttribute("task") Task task, Model model){
        Project project = projectService.findById(projectId);
        User currentUser = userService.getCurrentUser();

        if(!project.isLeader(currentUser)){
            return "error";
        }

        String errorMessage = task.errorIfDateIsInvalidForCreate();

        if(!errorMessage.equals("")){
            model.addAttribute("user", currentUser);
            model.addAttribute("error", errorMessage);
            return "task-create-panel";
        }

        task.setProject(project);
        task.setStatus(Status.IN_PROGRESS);
        taskService.save(task);

        return "redirect:/projects/" + project.getId();
    }

    @GetMapping("/tasks/{taskId}")
    public String getTask(@PathVariable int taskId, Model model){
        Task task = taskService.findById(taskId);
        Project project = task.getProject();
        User currentUser = userService.getCurrentUser();

        if(!project.isLeader(currentUser)){
            if(!project.hasUser(currentUser)) {
                return "error";
            } else {
                if(!project.isTaskVisibility() && !task.hasUser(currentUser)) {
                    return "error";
                }
            }
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("task", task);
        model.addAttribute("report", new Report());
        model.addAttribute("comment", new Comment());
        return "task-panel";
    }

    @PostMapping("/tasks/{taskId}/updateTaskStatus/{status}")
    public String updateTaskStatus(@PathVariable int taskId, @PathVariable String status){
        Task task = taskService.findById(taskId);
        User currentUser = userService.getCurrentUser();
        Project project = task.getProject();

        if(!(project.isLeader(currentUser) || task.hasUser(currentUser))){
            return "error";
        }

        taskService.updateTaskStatus(currentUser, task, Status.valueOf(status));

        return "redirect:/tasks/" + taskId;
    }

    @PostMapping("/tasks/{taskId}/deleteTask")
    public String deleteTask(@PathVariable int taskId){
        int projectId = taskService.findById(taskId).getProject().getId();
        User currentUser = userService.getCurrentUser();
        Project project = projectService.findById(projectId);

        if(!project.isLeader(currentUser)){
            return "error";
        }

        taskService.deleteById(taskId);

        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/tasks/{taskId}/editTaskPanel")
    public String editTaskPanel(@PathVariable int taskId, Model model){
        Task task = taskService.findById(taskId);
        User currentUser = userService.getCurrentUser();
        Project project = task.getProject();

        if(!project.isLeader(currentUser)){
            return "error";
        }

        model.addAttribute("task", task);
        model.addAttribute("user", currentUser);
        model.addAttribute("assignedUsers", task.getUsers());
        model.addAttribute("projectUsers", projectService.getUsersFromProjectExcludingTaskUsers(project, task));
        return "task-edit-panel";
    }

    @PostMapping("/tasks/{taskId}/editTask")
    public String editTask(@PathVariable int taskId, @ModelAttribute("task") Task task, Model model){
        User currentUser = userService.getCurrentUser();
        Task oldTask = taskService.findById(taskId);
        Project project = task.getProject();

        if(!project.isLeader(currentUser)){
            return "error";
        }

        String errorMessage = task.errorIfDateIsInvalidForEdit(oldTask);

        if(!errorMessage.equals("")){
            model.addAttribute("user", currentUser);
            model.addAttribute("error", errorMessage);
            model.addAttribute("assignedUsers", oldTask.getUsers());
            model.addAttribute("projectUsers", projectService.getUsersFromProjectExcludingTaskUsers(project, oldTask));
            return "task-edit-panel";
        }

        task.setUsers(oldTask.getUsers());
        taskService.save(task);

        return "redirect:/tasks/" + task.getId();
    }

    @PostMapping("/tasks/{taskId}/addUserToTask")
    public String addUserToTask(@PathVariable int taskId, @RequestParam int userId){
        User user = userService.findById(userId);
        Task task = taskService.findById(taskId);
        User currentUser = userService.getCurrentUser();
        Project project = task.getProject();

        if(!project.isLeader(currentUser)){
            return "error";
        }

        taskService.addUserToTask(user, task);

        return String.format("redirect:/tasks/%d/editTaskPanel", taskId);
    }

    @PostMapping("/tasks/{taskId}/deleteUserFromTask")
    public String deleteUserFromTask(@PathVariable int taskId, @RequestParam int userId){
        User user = userService.findById(userId);
        Task task = taskService.findById(taskId);
        User currentUser = userService.getCurrentUser();
        Project project = task.getProject();

        if(!project.isLeader(currentUser)){
            return "error";
        }

        taskService.deleteUserFromTask(user, task);

        return String.format("redirect:/tasks/%d/editTaskPanel", taskId);
    }

    @GetMapping("/tasks")
    public String getTasks(@RequestParam(required = false, defaultValue = "ALL") String taskStatus,
                           Model model){
        User user = userService.getCurrentUser();
        List<Task> tasks = taskService.findAllByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("taskStatus", taskStatus);

        if(taskStatus == null){
            taskStatus = "ALL";
        }

        if(taskStatus.equals("ALL")){
            model.addAttribute("tasks", tasks);
        } else {
            model.addAttribute("tasks",
                    taskService.findByTasksAndTaskStatus(tasks, Status.valueOf(taskStatus)));
        }

        return "tasks-panel";
    }
}

