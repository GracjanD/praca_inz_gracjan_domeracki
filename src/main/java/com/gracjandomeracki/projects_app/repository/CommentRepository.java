package com.gracjandomeracki.projects_app.repository;

import com.gracjandomeracki.projects_app.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
