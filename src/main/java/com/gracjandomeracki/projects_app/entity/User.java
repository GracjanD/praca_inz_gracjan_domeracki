package com.gracjandomeracki.projects_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "E-mail nie może być pusty!")
    @Size(max = 50, message = "E-mail jest zbyt długi!")
    @Email(message = "Niepoprawny format e-mail!")
    private String email;
    @NotBlank(message = "Nazwa użytkownika nie może być pusta!")
    @Size(max = 50, message = "Nazwa użytkownika jest zbyt długa!")
    private String username;
    @NotBlank(message = "Imię nie może być puste!")
    @Size(max = 50, message = "Imię jest zbyt długie!")
    @Column(name= "first_name")
    private String firstName;
    @NotBlank(message = "Nazwisko nie może być puste!")
    @Size(max = 50, message = "Nazwisko jest zbyt długie!")
    @Column(name= "last_name")
    private String lastName;

    @NotBlank(message = "Hasło nie może być puste!")
    @Size(min = 8, max = 70, message = "Hasło musi posiadać od 8-70 znaków!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()]).*$",
            message = "Hasło musi posiadać małą literę, dużą literę, cyfrę i znak specjalny!")
    private String password;

    @OneToMany(mappedBy = "leader")
    private List<Project> projects;

    @OneToMany(mappedBy = "author")
    private List<Invitation> invitationsByAuthor;

    @OneToMany(mappedBy = "invitedUser")
    private List<Invitation> invitationsByInvitedUser;

    @ManyToMany
    @JoinTable(
            name="project_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> userProjects;

    @ManyToMany
    @JoinTable(
            name = "task_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks;

    @OneToMany(mappedBy = "author")
    private List<Report> reports;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    public User(){}
    public User(String email, String username, String firstName, String lastName, String password){
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Invitation> getInvitationsByAuthor() {
        return invitationsByAuthor;
    }

    public void setInvitationsByAuthor(List<Invitation> invitationsByAuthor) {
        this.invitationsByAuthor = invitationsByAuthor;
    }

    public List<Invitation> getInvitationsByInvitedUser() {
        return invitationsByInvitedUser;
    }

    public void setInvitationsByInvitedUser(List<Invitation> invitationsByInvitedUser) {
        this.invitationsByInvitedUser = invitationsByInvitedUser;
    }

    public List<Project> getUserProjects() {
        return userProjects;
    }

    public void setUserProjects(List<Project> userProjects) {
        this.userProjects = userProjects;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String formatUserForView(){
        return String.format("%s %s (%s)", firstName, lastName, username);
    }
}
