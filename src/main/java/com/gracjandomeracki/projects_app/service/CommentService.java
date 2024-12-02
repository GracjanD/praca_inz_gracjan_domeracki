package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAll();
    Comment findById(int id);
    Comment save(Comment comment);
    String deleteById(int id);
}
