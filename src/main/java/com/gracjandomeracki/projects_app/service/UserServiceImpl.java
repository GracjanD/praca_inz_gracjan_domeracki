package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Project;
import com.gracjandomeracki.projects_app.entity.User;
import com.gracjandomeracki.projects_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika z id: " + id));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<Project> getUserProjects(User user) {
        return user.getUserProjects();
    }

    @Override
    public List<Project> getLeaderProjects(User leader) {
        return leader.getProjects();
    }

    @Transactional
    @Override
    public User save(User user) {
        user.setFirstName(formatName(user.getFirstName()));
        user.setLastName(formatName(user.getLastName()));

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        user.setUsername(user.getUsername().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    private String formatName(String name){
        name = name.trim();
        String firstLetter = name.substring(0,1).toUpperCase();
        String restOfName = name.substring(1).toLowerCase();

        return firstLetter + restOfName;
    }

    @Transactional
    @Override
    public String deleteById(int id) {
        User user = findById(id);
        userRepository.delete(user);

        return "Usunięto użytkownika: " + user.getEmail();
    }

    @Override
    public boolean isDataAvailable(User user) {
        return userRepository.findByEmail(user.getEmail()) == null && userRepository.findByUsername(user.getUsername().trim().toLowerCase()) == null;
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return findByUsername(username);
    }
}
