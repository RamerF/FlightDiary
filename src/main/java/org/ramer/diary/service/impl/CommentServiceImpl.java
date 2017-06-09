package org.ramer.diary.service.impl;

import org.ramer.diary.domain.Comment;
import org.ramer.diary.repository.CommentRepository;
import org.ramer.diary.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public void comment(Comment comment) {
        commentRepository.saveAndFlush(comment);
    }

    @Override
    @Transactional
    public boolean deleteComment(Comment comment) {
        Comment c = commentRepository.getByIdAndTopic(comment.getId(), comment.getTopic());
        if (c == null) {
            return false;
        }
        commentRepository.delete(comment.getId());
        return true;

    }

}