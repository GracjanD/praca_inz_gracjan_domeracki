package com.gracjandomeracki.projects_app.repository;

import com.gracjandomeracki.projects_app.entity.Report;
import com.gracjandomeracki.projects_app.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findAllByTask(Task task);
}
