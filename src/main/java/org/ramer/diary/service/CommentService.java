package org.ramer.diary.service;

import org.ramer.diary.domain.Comment;
import org.ramer.diary.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

  @Autowired
  private CommentRepository commentRepository;

  /**
   * 用户评论
   * @param comment 用户评论
   */
  @Transactional
  public void comment(Comment comment) {
    commentRepository.saveAndFlush(comment);
  }

  /**
   * 删除评论
   * @param comment 用户评论
   * @return 删除评论成功返回true
   */
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