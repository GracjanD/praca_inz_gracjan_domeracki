package com.gracjandomeracki.projects_app.service;

import com.gracjandomeracki.projects_app.entity.Comment;
import com.gracjandomeracki.projects_app.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }


    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(int id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono komentarza o id: " + id));
    }

    @Transactional
    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public String deleteById(int id) {
        commentRepository.deleteById(id);
        return "Usunięto komentarz o id: " + id;
    }
}
