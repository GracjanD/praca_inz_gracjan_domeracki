package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.Task;
import com.gracjandomeracki.projects_app.entity.User;
import com.gracjandomeracki.projects_app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project findById(int id) {
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono projektu z id: " + id));
    }

    @Transactional
    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    @Override
    public String deleteById(int id) {
        Project project = findById(id);
        projectRepository.delete(project);

        return "Usunięto projekt: " + project.getTitle();
    }

    @Transactional
    @Override
    public String addUserToProject(int projectId, User user){
        Project project = findById(projectId);
        if(project.getUsers().contains(user)){
            return "Użytkownik znajduje się już w projekcie!";
        }

        project.getUsers().add(user);
        projectRepository.save(project);
        return "Dodano użytkownika '" + user.getUsername() + "' do projektu '" + project.getTitle() + "'.";
    }

    @Override
    public List<User> getUsersFromProject(Project project) {
        return project.getUsers();
    }

    @Transactional
    @Override
    public String deleteUserFromProject(User user, Project project) {
        return project.getUsers().remove(user) ? "Pomyślnie usunięto użytkownika" : "Nie udało się usunąć użytkownika";
    }

    @Transactional
    @Override
    public String updateProjectLeader(User newLeader, Project project) {

        User previousLeader = project.getLeader();
        addUserToProject(project.getId(), previousLeader);

        project.setLeader(newLeader);
        deleteUserFromProject(newLeader, project);
        return "Nowym liderem dla projektu '" + project.getTitle() + "' został użytkownik '" + newLeader.getUsername() + "'";
    }

    @Override
    public List<User> getUsersFromProjectExcludingTaskUsers(Project project, Task task) {
        List<User> projectUsers = getUsersFromProject(project);
        List<User> users = new ArrayList<>();

        for(var projectUser : projectUsers){
            if(task.getUsers().contains(projectUser)){
                continue;
            }
            users.add(projectUser);
        }
        return users;
    }
}
