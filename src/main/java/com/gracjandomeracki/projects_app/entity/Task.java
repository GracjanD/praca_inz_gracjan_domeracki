package com.gracjandomeracki.projects_app.entity;

import com.gracjandomeracki.projects_app.util.DateValidation;
import jakarta.persistence.*;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "task_user",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Report> reports;

    public Task(){}

    public Task(int id, String title, String description, Status status, LocalDate startDate, LocalDate endDate, Priority priority, Project project){
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.project = project;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getStatusString(){
        return switch(status){
            case COMPLETED -> "Ukończony";
            case IN_PROGRESS -> "W trakcie";
            case TO_BE_CHECKED -> "Do sprawdzenia";
        };
    }

    public String getPriorityString(){
        return switch(priority){
            case HIGH -> "WYSOKI";
            case MEDIUM -> "ŚREDNI";
            case LOW -> "NISKI";
        };
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public String showTaskUsersInString(Task task){
        List<User> taskUsers = task.getUsers();
        int usersCount = taskUsers.size();
        String[] users = new String[usersCount];

        for(int i = 0; i < usersCount; i++){
            users[i] = taskUsers.get(i).getFirstName() + " " + taskUsers.get(i).getLastName();
        }

        return String.join(", ", users);
    }

    public int countRemainingDaysToDeadline(){
        LocalDate deadline = endDate;
        LocalDate now = LocalDate.now();

        if(deadline.isBefore(now)){
            return 0;
        }

        return (int) ChronoUnit.DAYS.between(now, deadline);
    }

    public String errorIfDateIsInvalidForCreate(){
        return DateValidation.errorIfDateIsInvalidForCreate(startDate, endDate);
    }

    public String errorIfDateIsInvalidForEdit(Task oldTask){
        return DateValidation.errorIfDateIsInvalidForEdit(startDate, endDate, oldTask.startDate);
    }

    public boolean hasUser(User user) {
        return users.contains(user);
    }
}
