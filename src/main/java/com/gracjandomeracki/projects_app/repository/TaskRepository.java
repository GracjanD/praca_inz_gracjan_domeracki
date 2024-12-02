package com.gracjandomeracki.projects_app.repository;

import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.Task;
import com.gracjandomeracki.projects_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByProject(Project project);
    List<Task> findAllByProjectAndUsers(Project project, User user);
}
