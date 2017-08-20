package org.ramer.diary.repository;

import org.ramer.diary.domain.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by caipeijun on 2017/5/29.
 */

public interface TagsRepository extends JpaRepository<Tags, Integer>{
    Tags getTagsByTagName(String tagName);
}
