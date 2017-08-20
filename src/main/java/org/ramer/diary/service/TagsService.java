package org.ramer.diary.service;

import org.ramer.diary.domain.Tags;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签相关服务
 * Created by caipeijun on 2017/5/29.
 */
@Service
public interface TagsService{

    Tags saveOrUpdate(Tags tags);

    Tags getById(Integer id);

    Tags getByName(String tagName);

    List<Tags> getAllTags();

    List<Tags> getTagsPage(int page, int size);
}
