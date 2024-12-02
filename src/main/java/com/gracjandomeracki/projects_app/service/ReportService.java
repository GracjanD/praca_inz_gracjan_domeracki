package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Report;
import com.gracjandomeracki.projects_app.entity.Task;
import com.gracjandomeracki.projects_app.entity.User;

import java.util.List;

public interface ReportService {
    List<Report> findAll();
    List<Report> findAllByTask(Task task);
    Report findById(int id);
    Report save(Report report);
    String deleteById(int id);
}
