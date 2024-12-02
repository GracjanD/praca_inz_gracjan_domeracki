package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.Task;
import com.gracjandomeracki.projects_app.entity.User;

import java.util.List;

public interface ProjectService {

    List<Project> findAll();
    Project findById(int id);
    Project save(Project project);
    String deleteById(int id);
    String addUserToProject(int projectId, User user);
    List<User> getUsersFromProject(Project project);
    String deleteUserFromProject(User user, Project project);
    String updateProjectLeader(User newLeader, Project project);
    List<User> getUsersFromProjectExcludingTaskUsers(Project project, Task task);
}
