package org.ramer.diary.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.*;
import org.ramer.diary.repository.TopicRepository;
import org.ramer.diary.service.*;
import org.ramer.diary.util.Pagination;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class TopicServiceImpl implements TopicService{
    @Resource
    private TopicRepository topicRepository;
    @Resource
    private AlbumsService albumsService;
    @Resource
    private TagsService tagsService;

    @Override
    @Transactional
    public Topic publish(User user, String content, String[] tags, String[] fileUrls) {
        Topic topic = new Topic();
        topic.setContent(content);
        topic.setDate(new Date());
        topic.setUser(user);
        topic.setUpCounts(0);
        Set<Tags> tagsList = new HashSet<>();
        topicRepository.saveAndFlush(topic);
        // 保存标签: 1. 已有: 标签热度加一,2. 没有: 新建标签
        for (String tag : tags) {
            Tags byName = tagsService.getByName(tag);
            if (byName == null) {
                byName = new Tags();
                byName.setHot(1);
                byName.setTagName(tag);
            } else {
                byName.setHot(byName.getHot() + 1);
            }
            tagsService.saveOrUpdate(byName);
            tagsList.add(byName);
        }
        topic.setTagses(tagsList);
        List<Albums> albumsList = new ArrayList<>();
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "  topic图片: [{}]", fileUrls);

        // 保存图片地址
        for (String fileUrl : fileUrls) {
            Albums albums = new Albums();
            albums.setTopic(topic);
            albums.setUrl(fileUrl);
            albumsService.save(albums);
            albumsList.add(albums);
        }
        topic.setAlbums(albumsList);
        topicRepository.saveAndFlush(topic);
        return topic;
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