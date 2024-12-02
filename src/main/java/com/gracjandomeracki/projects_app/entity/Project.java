package com.gracjandomeracki.projects_app.entity;

import com.gracjandomeracki.projects_app.util.DateValidation;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="projects")
public class Project {

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
    @ManyToOne
    @JoinColumn(name = "leader_id")
    private User leader;
    @Column(name = "task_visibility")
    private boolean taskVisibility;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Invitation> invitations;

    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = @JoinColumn(name="project_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private List<User> users;

    public Project(){}

    public Project(String title, String description, Status status, LocalDate startDate, LocalDate endDate, User leader, boolean taskVisibility){
        this.title = title;
        this.description = description;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leader = leader;
        this.taskVisibility = taskVisibility;
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

    public String getStatusString(){
        return switch(status){
            case COMPLETED -> "Ukończony";
            case IN_PROGRESS -> "W trakcie";
            case TO_BE_CHECKED -> "Do sprawdzenia";
        };
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

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public boolean isTaskVisibility() {
        return taskVisibility;
    }

    public void setTaskVisibility(boolean taskVisibility) {
        this.taskVisibility = taskVisibility;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int countTasksByStatus(String taskStatus){
        int countOfTasksByStatus = 0;

        for(var task : tasks){
            if(taskStatus.equals(task.getStatusString())){
                countOfTasksByStatus++;
            }
        }
        return countOfTasksByStatus;
    }

    public double percentOfTasksByStatus(String taskStatus){
        double countOfTasks = tasks.size();
        double countOfTasksByStatus = countTasksByStatus(taskStatus);

        if(countOfTasks == 0){
            return 0;
        }
        return (countOfTasksByStatus / countOfTasks) * 100;
    }

    public String getTasksSummary(String taskStatus){
        int countTasksByStatus = countTasksByStatus(taskStatus);
        double percentOfTasksByStatus = percentOfTasksByStatus(taskStatus);

        String statusMessage = switch (taskStatus){
            case "COMPLETED" -> "ukończono ";
            case "TO_BE_CHECKED" -> "do sprawdzenia ";
            case "IN_PROGRESS" -> "w trakcie ";
            default -> "błąd";
        };

        if(percentOfTasksByStatus == (int) percentOfTasksByStatus){
            return String.format("%s %.0f%% (l.zadań: %d)", statusMessage, percentOfTasksByStatus, countTasksByStatus);
        }

        return String.format("%s %.1f%% (l.zadań: %d)", statusMessage, percentOfTasksByStatus, countTasksByStatus);
    }

    public int countTasksByStatusAndUser(String taskStatus, User user){
        int countOfTasksByStatus = 0;

        for(var task : tasks){
            if(taskStatus.equals(task.getStatusString()) && task.hasUser(user)){
                countOfTasksByStatus++;
            }
        }

        return countOfTasksByStatus;
    }

    public double percentOfTasksByStatusAndUser(String taskStatus, User user){
        double countOfTasks = 0;
        double countOfTasksByStatus = countTasksByStatusAndUser(taskStatus, user);

        for(var task : tasks){
            if(task.hasUser(user)){
                countOfTasks++;
            }
        }

        if(countOfTasks == 0){
            return 0;
        }

        return (countOfTasksByStatus / countOfTasks) * 100;
    }

    public String getTasksByUserSummary(String taskStatus, User user){
        int countTasksByStatus = countTasksByStatusAndUser(taskStatus, user);
        double percentOfTasksByStatus = percentOfTasksByStatusAndUser(taskStatus, user);

        String statusMessage = switch (taskStatus){
            case "COMPLETED" -> "ukończono ";
            case "TO_BE_CHECKED" -> "do sprawdzenia ";
            case "IN_PROGRESS" -> "w trakcie ";
            default -> "błąd";
        };

        if(percentOfTasksByStatus == (int) percentOfTasksByStatus){
            return String.format("%s %.0f%% (l.zadań: %d)", statusMessage, percentOfTasksByStatus, countTasksByStatus);
        }
        return String.format("%s %.1f%% (l.zadań: %d)", statusMessage, percentOfTasksByStatus, countTasksByStatus);
    }

    public Task getTaskByTheNearestDeadline(){

        if(tasks.isEmpty()){
            return null;
        }

        Task nearestTask = tasks.get(0);

        for(var task : tasks){
            if(task.getEndDate().isBefore(nearestTask.getEndDate())){
                nearestTask = task;
            }
        }

        return nearestTask;
    }

    public String parsePercent(double percent){
        return (percent == (int) percent) ? String.format("%.0f", percent) : String.format("%.1f", percent);
    }

    public String errorIfDateIsInvalidForCreate(){
        return DateValidation.errorIfDateIsInvalidForCreate(startDate, endDate);
    }

    public String errorIfDateIsInvalidForEdit(Project oldProject){
        return DateValidation.errorIfDateIsInvalidForEdit(startDate, endDate, oldProject.startDate);
    }

    public boolean isLeader(User user) {
        return leader.equals(user);
    }

    public boolean hasUser(User user) {
        return users.contains(user);
    }
}
