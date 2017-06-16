package org.ramer.diary.service;

import org.ramer.diary.domain.Tags;
import org.springframework.data.annotation.Id;

/**
 * 标签相关服务
 * Created by caipeijun on 2017/5/29.
 */
public interface TagsService {

    Tags findOne(Integer id);

    Tags findOneByName(String name);

}
