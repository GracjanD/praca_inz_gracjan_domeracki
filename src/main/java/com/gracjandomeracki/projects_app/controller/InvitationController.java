package com.gracjandomeracki.projects_app.controller;

import com.gracjandomeracki.projects_app.entity.Invitation;
import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.User;
import com.gracjandomeracki.projects_app.service.InvitationService;
import com.gracjandomeracki.projects_app.service.ProjectService;
import com.gracjandomeracki.projects_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/invitations")
public class InvitationController {
    private InvitationService invitationService;
    private ProjectService projectService;
    private UserService userService;

    @Autowired
    public InvitationController(InvitationService invitationService, ProjectService projectService, UserService userService){
        this.invitationService = invitationService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @PostMapping("/acceptInvitation")
    public String acceptInvitation(@RequestParam int invitationId){
        Invitation invitation = invitationService.findById(invitationId);
        int projectId = invitation.getProject().getId();

        projectService.addUserToProject(projectId, userService.getCurrentUser());
        invitationService.deleteById(invitationId);

        return "redirect:/projects";
    }

    @PostMapping("/cancelInvitation")
    public String cancelInvitation(@RequestParam int invitationId){

        invitationService.deleteById(invitationId);

        return "redirect:/projects";
    }

    @PostMapping("/inviteUser")
    public String inviteUser(@RequestParam String username, @RequestParam int projectId, Model model){
        User user = userService.findByUsername(username);
        Project project = projectService.findById(projectId);

        if(!project.isLeader(userService.getCurrentUser())){
            return "error";
        }

        int errorNum = invitationService.errorIfUserCanNotBeInvited(user, project);
        if(errorNum > 0){
            model.addAttribute("errorMessage", errorNum);
            return "redirect:/projects/" + projectId + "?error" + errorNum;
        }

        invitationService.save(new Invitation(project, userService.getCurrentUser(), user));
        return "redirect:/projects/" + projectId + "?success";
    }
}
