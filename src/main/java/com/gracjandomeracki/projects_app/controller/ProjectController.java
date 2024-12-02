package com.gracjandomeracki.projects_app.controller;

import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.Status;
import com.gracjandomeracki.projects_app.entity.Task;
import com.gracjandomeracki.projects_app.entity.User;
import com.gracjandomeracki.projects_app.service.ProjectService;
import com.gracjandomeracki.projects_app.service.TaskService;
import com.gracjandomeracki.projects_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private ProjectService projectService;
    private UserService userService;
    private TaskService taskService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService, TaskService taskService){
        this.projectService = projectService;
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping
    public String getProjects(Model model){
        User currentUser = userService.getCurrentUser();
        model.addAttribute("userProjects", currentUser.getUserProjects());
        model.addAttribute("projects", currentUser.getProjects());
        model.addAttribute("user", currentUser);
        model.addAttribute("invitations", currentUser.getInvitationsByInvitedUser());

        return "projects-panel";
    }

    @GetMapping("/{projectId}")
    public String getProject(@RequestParam(required = false, defaultValue = "ALL") String taskStatus,
            @PathVariable int projectId, Model model){

        Project project = projectService.findById(projectId);
        User currentUser = userService.getCurrentUser();

        if(!(project.isLeader(currentUser) || project.hasUser(currentUser))){
            return "error";
        }

        List<Task> userTasks = taskService.findAllByUserAndProject(currentUser, project);
        List<Task> otherUsersTasks = taskService.findAllByProjectExcludingUser(project, currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("project", project);
        model.addAttribute("taskStatus", taskStatus);

        if(taskStatus == null){
            taskStatus = "ALL";
        }

        if(taskStatus.equals("ALL")){
            model.addAttribute("otherUsersTasks", otherUsersTasks);
            model.addAttribute("userTasks", userTasks);
        } else {
            model.addAttribute("userTasks", taskService.findByTasksAndTaskStatus(userTasks, Status.valueOf(taskStatus)));
            model.addAttribute("otherUsersTasks", taskService.findByTasksAndTaskStatus(otherUsersTasks, Status.valueOf(taskStatus)));
        }

        return "project-panel";
    }

    @GetMapping("/createProject")
    public String createProject(Model model){
        model.addAttribute("project", new Project());
        model.addAttribute("user", userService.getCurrentUser());
        return "project-create-panel";
    }

    @PostMapping("/saveProject")
    public String saveProject(@ModelAttribute("project") Project project, Model model){

        String errorMessage = project.errorIfDateIsInvalidForCreate();

        if(!errorMessage.equals("")){
            model.addAttribute("user", userService.getCurrentUser());
            model.addAttribute("error", errorMessage);
            return "project-create-panel";
        }

        project.setLeader(userService.getCurrentUser());
        project.setStatus(Status.IN_PROGRESS);
        projectService.save(project);

        return "redirect:/projects";
    }

    @GetMapping("/{projectId}/editProjectPanel")
    public String editProjectPanel(@PathVariable int projectId, Model model){
        Project project = projectService.findById(projectId);
        User currentUser = userService.getCurrentUser();

        if(!project.isLeader(currentUser)){
            return "error";
        }

        model.addAttribute("project", project);
        model.addAttribute("user", currentUser);
        model.addAttribute("projectUsers", project.getUsers());
        return "project-edit-panel";
    }

    @PostMapping("/{projectId}/editProject")
    public String editProject(@PathVariable int projectId, @ModelAttribute("project") Project project, Model model){
        User currentUser = userService.getCurrentUser();
        Project oldProject = projectService.findById(projectId);

        if(!oldProject.isLeader(currentUser)){
            return "error";
        }

        String errorMessage = project.errorIfDateIsInvalidForEdit(oldProject);

        if(!errorMessage.equals("")){
            model.addAttribute("user", currentUser);
            model.addAttribute("error", errorMessage);
            model.addAttribute("projectUsers", oldProject.getUsers());
            return "project-edit-panel";
        }

        project.setUsers(oldProject.getUsers());
        projectService.save(project);

        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/updateProjectStatus/{status}")
    public String updateProjectStatus(@PathVariable int projectId, @PathVariable String status){
        Project project = projectService.findById(projectId);
        User currentUser = userService.getCurrentUser();

        if(!project.isLeader(currentUser)){
            return "error";
        }

        project.setStatus(Status.valueOf(status));
        projectService.save(project);

        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/deleteProject")
    public String deleteProject(@PathVariable int projectId){
        User currentUser = userService.getCurrentUser();
        Project project = projectService.findById(projectId);

        if(!project.isLeader(currentUser)){
            return "error";
        }

        projectService.deleteById(projectId);

        return "redirect:/projects";
    }

    @PostMapping("/{projectId}/deleteUserFromProject")
    public String deleteUserFromProject(@PathVariable int projectId, @RequestParam int userId){
        Project project = projectService.findById(projectId);
        User currentUser = userService.getCurrentUser();

        if(!project.isLeader(currentUser)){
            return "error";
        }

        User user = userService.findById(userId);
        projectService.deleteUserFromProject(user, project);

        return String.format("redirect:/projects/%d/editProjectPanel", projectId);
    }

    @PostMapping("/{projectId}/updateProjectLeader")
    public String updateProjectLeader(@PathVariable int projectId, @RequestParam int userId){
        Project project = projectService.findById(projectId);
        User currentUser = userService.getCurrentUser();

        if(!project.isLeader(currentUser)){
            return "error";
        }

        User user = userService.findById(userId);
        user.getTasks().removeAll(project.getTasks());
        projectService.updateProjectLeader(user, project);

        return String.format("redirect:/projects/%d", projectId);
    }
}
