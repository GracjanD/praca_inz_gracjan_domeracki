package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Report;
import com.gracjandomeracki.projects_app.entity.Task;
import com.gracjandomeracki.projects_app.entity.User;
import com.gracjandomeracki.projects_app.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{

    private ReportRepository reportRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository){
        this.reportRepository = reportRepository;
    }


    @Override
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    @Override
    public List<Report> findAllByTask(Task task) {
        return reportRepository.findAllByTask(task);
    }

    @Override
    public Report findById(int id) {
        return reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono raportu o id: " + id));
    }

    @Transactional
    @Override
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Transactional
    @Override
    public String deleteById(int id) {
        reportRepository.deleteById(id);
        return "Usunięto raport o id: " + id;
    }
}
