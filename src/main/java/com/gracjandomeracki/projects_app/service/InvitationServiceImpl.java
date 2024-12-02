package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Invitation;
import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.User;
import com.gracjandomeracki.projects_app.repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvitationServiceImpl implements InvitationService{

    private InvitationRepository invitationRepository;

    @Autowired
    public InvitationServiceImpl(InvitationRepository invitationRepository){
        this.invitationRepository = invitationRepository;
    }

    @Override
    public List<Invitation> findAll() {
        return invitationRepository.findAll();
    }

    @Override
    public Invitation findById(int id) {
        return invitationRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono zaproszenia z id: " + id));
    }

    @Transactional
    @Override
    public Invitation save(Invitation invitation) {
        return invitationRepository.save(invitation);
    }

    @Transactional
    @Override
    public String deleteById(int id) {
        Invitation invitation = findById(id);
        invitationRepository.deleteById(id);
        return "Usunięto zaproszenie z id: " + id;
    }

    @Override
    public List<Invitation> findAllByUserAndProject(User user, Project project) {
        System.out.println(invitationRepository.findAllByInvitedUserAndProject(user, project).toString());
        return invitationRepository.findAllByInvitedUserAndProject(user, project);
    }

    @Override
    public int errorIfUserCanNotBeInvited(User user, Project project) {
        int errorNum = 0;

        if(user == null){
            errorNum = 1;
        } else if(!(findAllByUserAndProject(user, project).isEmpty())){
            errorNum = 2;
        } else if((project.hasUser(user)) || (project.getLeader().getUsername().equals(user.getUsername()))){
            errorNum = 3;
        }

        return errorNum;
    }

    @Override
    public List<Invitation> findAllByUser(User user){
        List<Invitation> invitations = invitationRepository.findAllByInvitedUser(user);
        return invitations;
    }


}
