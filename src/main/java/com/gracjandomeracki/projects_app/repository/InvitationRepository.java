package com.gracjandomeracki.projects_app.repository;

import com.gracjandomeracki.projects_app.entity.Invitation;
import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {
    List<Invitation> findAllByInvitedUser(User user);
    List<Invitation> findAllByInvitedUserAndProject(User user, Project project);
}
