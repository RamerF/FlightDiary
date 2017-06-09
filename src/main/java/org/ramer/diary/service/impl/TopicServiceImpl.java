package org.ramer.diary.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.TopicRepository;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
@Service
public class TopicServiceImpl implements TopicService {

    @Resource
    private TopicRepository topicRepository;

    @Override
    @Transactional
    public Topic publish(Topic topic) {
        Topic t = new Topic();
        try {
            t = topicRepository.save(topic);
        } catch (Exception exception) {
            return new Topic();
        }
        return t;
    }

    @Override
    @Transactional
    public void deleteTopic(Topic topic) {
        log.debug("删除topic");
        topicRepository.delete(topic.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllTags() {
        List<String> cities = topicRepository.getOrderedTags();
        return cities;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Topic> getTopicsPage(int page, int size) {
        //页号从零开始
        page = page - 1;
        //按时间排序
        Order orders = new Order(Direction.DESC, "date");
        Sort sort = new Sort(orders);
        Pageable pageable = new PageRequest(page, size, sort);
        return topicRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Topic> getTopicsPageOrderByFavourite(int page, int size) {
        //页号从零开始
        page -= 1;
        //按热度排序
        Order order = new Order(Direction.DESC, "upCounts");
        Sort sort = new Sort(order);
        Pageable pageable = new PageRequest(page, size, sort);
        return topicRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<Topic> getTopicsPageByTags(String tag, int page, int size) {
        page -= 1;
        int count = (int) getCountByTag(tag);
        int start = page * size;
        List<Topic> topics = topicRepository.getByTagsLimit(tag, start, size);
        if (topics.size() <= 0) {
            return new Pagination<>(new ArrayList<Topic>(), page, size, count);
        }
        Pagination<Topic> pageTopic = new Pagination<>(topics, page, size, count);
        return pageTopic;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Topic> getTopicsPageByUserId(User user, int page, int size) {
        //页号从零开始
        page -= 1;
        Pageable pageable = new PageRequest(page, size);
        return topicRepository.getByUserOrderByDateDesc(user, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Topic getTopicById(Integer topic_id) {
        return topicRepository.getById(topic_id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean getTopicByUserIdAndTopicId(Integer topic_id, User user) {
        Topic topic = topicRepository.getByIdAndUser(topic_id, user);
        if (topic == null) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public int getTopicNumber(User user) {
        int number = topicRepository.getCountByUser(user);
        return number > 0 ? number : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public long getCount() {
        return topicRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountByTag(String tag) {
        return topicRepository.getCountByTag(tag);
    }

}