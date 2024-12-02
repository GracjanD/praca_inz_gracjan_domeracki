package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Invitation;
import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.User;

import java.util.List;

public interface InvitationService {
    List<Invitation> findAll();
    List<Invitation> findAllByUser(User user);
    Invitation findById(int id);
    Invitation save(Invitation invitation);
    String deleteById(int id);
    List<Invitation> findAllByUserAndProject(User user, Project project);
    int errorIfUserCanNotBeInvited(User user, Project project);
}
