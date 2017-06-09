package org.ramer.diary.service.impl;

import org.ramer.diary.domain.Reply;
import org.ramer.diary.repository.ReplyRepository;
import org.ramer.diary.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ReplyServiceImpl implements ReplyService {

  @Resource
  private ReplyRepository replyRepository;

  @Override
  @Transactional
  public boolean replyComment(Reply reply) {
    replyRepository.saveAndFlush(reply);
    return true;
  }

  @Override
  @Transactional
  public void replyReply(Reply reply) {
    replyRepository.saveAndFlush(reply);
  }

  @Override
  @Transactional
  public boolean deleteReply(Integer id) {
    replyRepository.delete(id);
    return true;
  }

}