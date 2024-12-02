package com.gracjandomeracki.projects_app.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String message;
    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @Column(name = "creation_date")
    private LocalDate creationDate;

    public Comment(){}

    public Comment(String message, Report report, User author){
        this.message = message;
        this.report = report;
        this.author = author;
        creationDate = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isAuthor(User user){
        return author.equals(user);
    }
}
