package com.gracjandomeracki.projects_app.repository;

import com.gracjandomeracki.projects_app.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
