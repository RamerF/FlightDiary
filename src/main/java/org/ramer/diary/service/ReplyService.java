package org.ramer.diary.service;

import org.ramer.diary.domain.Reply;
import org.ramer.diary.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReplyService {

  @Autowired
  private ReplyRepository replyRepository;

  /**
   * 回复评论.
   *
   * @param reply 回复类
   * @return 回复成功返回true
   */
  @Transactional
  public boolean replyComment(Reply reply) {
    replyRepository.saveAndFlush(reply);
    return true;
  }

  /**
   * 回复一个回复
   *
   * @param reply 回复
   */
  @Transactional
  public void replyReply(Reply reply) {
    replyRepository.saveAndFlush(reply);
  }

  /**
   * 删除一个回复
   * @param id 回复ID
   * @return 操作成功返回true
   */
  @Transactional
  public boolean deleteReply(Integer id) {
    replyRepository.delete(id);
    return true;
  }

}