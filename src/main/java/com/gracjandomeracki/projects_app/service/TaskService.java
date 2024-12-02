package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.Status;
import com.gracjandomeracki.projects_app.entity.Task;
import com.gracjandomeracki.projects_app.entity.User;

import java.util.List;

public interface TaskService {
    List<Task> findAll();
    Task findById(int id);
    List<Task> findAllByProject(Project project);
    Task save(Task task);
    String deleteById(int id);
    List<Task> findAllByUserAndProject(User user, Project project);
    List<Task> findAllByProjectExcludingUser(Project project, User user);
    List<User> getUsers(Task task);
    String addUserToTask(User user, Task task);
    String deleteUserFromTask(User user, Task task);
    List<Task> findAllByUser(User user);
    List<Task> findByTasksAndTaskStatus(List<Task> tasks, Status status);
    boolean canChangeTaskStatus(User currentUser, Task task, Status status);
    void updateTaskStatus(User currentUser, Task task, Status status);
    boolean isTaskStatusEquals(Task task, Status status);
}
