package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.Status;
import com.gracjandomeracki.projects_app.entity.Task;
import com.gracjandomeracki.projects_app.entity.User;
import com.gracjandomeracki.projects_app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(int id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono zadania z id: " + id));
    }

    @Override
    public List<Task> findAllByProject(Project project) {
        return taskRepository.findAllByProject(project);
    }

    @Transactional
    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    @Override
    public String deleteById(int id) {
        Task task = findById(id);
        taskRepository.delete(task);

        return "Usunięto zadanie: " + task.getTitle();
    }

    @Override
    public List<Task> findAllByUserAndProject(User user, Project project) {

        return taskRepository.findAllByProjectAndUsers(project, user);
    }

    @Override
    public List<Task> findAllByProjectExcludingUser(Project project, User user) {
        List<Task> projectTasks = findAllByProject(project);
        List<Task> tasks = new ArrayList<>();

        for(var task : projectTasks){
            if(task.getUsers().contains(user)){
                continue;
            }
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    public List<User> getUsers(Task task) {
        return task.getUsers();
    }

    @Transactional
    @Override
    public String addUserToTask(User user, Task task) {
        task.getUsers().add(user);
        return "Dodano użytkownika '" + user.getUsername() + "' do zadania '" + task.getTitle() + "'";
    }

    @Transactional
    @Override
    public String deleteUserFromTask(User user, Task task) {
        task.getUsers().remove(user);
        return "Usunięto użytkownika '" + user.getUsername() + "' z zadania '" + task.getTitle() + "'";
    }

    @Override
    public List<Task> findAllByUser(User user) {
        return user.getTasks();
    }

    @Override
    public List<Task> findByTasksAndTaskStatus(List<Task> tasks, Status status) {
        List<Task> tasksByStatus = new ArrayList<>();

        for(var task : tasks){
            if(isTaskStatusEquals(task, status)){
                tasksByStatus.add(task);
            }
        }
        return tasksByStatus;
    }

    @Override
    public boolean canChangeTaskStatus(User currentUser, Task task, Status status) {
        Project project = task.getProject();
        boolean isUserInTask = task.hasUser(currentUser);
        boolean isUserTheLeaderOfProject = project.isLeader(currentUser);

        boolean isTaskStatusCompleted = isTaskStatusEquals(task, Status.COMPLETED);
        boolean isTaskStatusToBeChecked = isTaskStatusEquals(task, Status.TO_BE_CHECKED);

        return switch(status){
            case COMPLETED -> isUserTheLeaderOfProject && !isTaskStatusCompleted;
            case TO_BE_CHECKED -> isUserInTask && !(isTaskStatusCompleted || isTaskStatusToBeChecked);
            case IN_PROGRESS -> (isUserInTask && isTaskStatusToBeChecked) ||
                    isUserTheLeaderOfProject && (isTaskStatusToBeChecked ||
                            isTaskStatusCompleted);
        };
    }

    @Override
    public void updateTaskStatus(User currentUser, Task task, Status status) {
        if(canChangeTaskStatus(currentUser, task, status)){
            task.setStatus(status);
            save(task);
        }
    }

    @Override
    public boolean isTaskStatusEquals(Task task, Status status){
        if (task == null || status == null){
            return false;
        }

        Status taskStatus = task.getStatus();
        if(taskStatus == null){
            return false;
        }

        return taskStatus.equals(status);
    }
}
