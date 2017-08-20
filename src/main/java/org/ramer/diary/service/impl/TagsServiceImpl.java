package org.ramer.diary.service.impl;

import org.ramer.diary.domain.Tags;
import org.ramer.diary.repository.TagsRepository;
import org.ramer.diary.service.TagsService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by caipeijun on 2017/5/29.
 */
@Service
public class TagsServiceImpl implements TagsService{
    @Resource
    private TagsRepository tagsRepository;

    @Override
    public synchronized Tags saveOrUpdate(Tags tags) {
        return tagsRepository.saveAndFlush(tags);
    }

    @Transactional(readOnly = true)
    @Override
    public Tags getById(Integer id) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Tags getByName(String tagName) {
        return tagsRepository.getTagsByTagName(tagName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tags> getAllTags() {
        return tagsRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tags> getTagsPage(int page, int size) {
        Pageable pageable = new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        return tagsRepository.findAll(pageable).getContent();
    }
}
