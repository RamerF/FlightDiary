package org.ramer.diary.service.impl;

import org.ramer.diary.domain.*;
import org.ramer.diary.repository.PraiseRepository;
import org.ramer.diary.repository.TopicRepository;
import org.ramer.diary.service.PraiseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PraiseServiceImpl implements PraiseService{

    @Resource
    private PraiseRepository praiseRepository;
    @Resource
    private TopicRepository topicRepository;

    @Override
    @Transactional
    public List<Integer> getPraiseTopicIds(User user, User other) {
        return praiseRepository.getTopicIdsByUserIdAndOtherId(user.getId(), other.getId());

    }

    @Override
    @Transactional
    public boolean praise(User user, Topic topic) {

        // 判断是否已点过赞
        Praise p = praiseRepository.getByUserAndTopic(user, topic);
        //如果不为空说明用户已点过赞,取消本次操作
        if (p != null) {
            return false;
        }
        //将分享点赞次数加1
        topicRepository.updateTopicPraise(topic.getId());
        //更新点赞表
        Praise praise = new Praise();
        praise.setUser(user);
        praise.setTopic(topic);
        praiseRepository.saveAndFlush(praise);
        return true;
    }

    @Override
    @Transactional
    public boolean notPraise(Topic topic, User user) {
        // 判断是否已点过赞
        Praise p = praiseRepository.getByUserAndTopic(user, topic);
        //如果为空说明用户已取消点赞,取消本次操作
        if (p == null) {
            return false;
        }
        //将点赞次数-1
        topicRepository.updateTopicNotPraise(topic.getId());
        praiseRepository.deleteByTopicAndUser(topic, user);
        return true;
    }
}