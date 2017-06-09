package org.ramer.diary.service;

import org.ramer.diary.domain.Comment;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by RAMER on 5/18/2017.
 */
public interface CommentService {
    @Transactional
    void comment(Comment comment);

    @Transactional
    boolean deleteComment(Comment comment);
}
