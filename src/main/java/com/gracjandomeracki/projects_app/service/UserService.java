package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();
    User findById(int id);
    User findByUsername(String username);
    List<Project> getUserProjects(User user);
    List<Project> getLeaderProjects(User leader);
    User save(User user);
    String deleteById(int id);
    boolean isDataAvailable(User user);
    User getCurrentUser();
    User registerUser(User user);
}
