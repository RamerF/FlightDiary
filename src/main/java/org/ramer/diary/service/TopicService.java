package org.ramer.diary.service;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.util.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by RAMER on 5/17/2017.
 */
public interface TopicService{
    /**
     * 用户发表日记
     * @param topic 用户分享,日记
     * @return 发表分享成功返回true
     */
    Topic publish(Topic topic);

    /**
     * 删除分享
     * @param topic 用户分享,日记
     */
    void deleteTopic(Topic topic);

    /**
     * 获取所有的标签，并根据出现的次数排序
     * @return 所有标签的集合
     */
    List<String> getAllTags();

    /**
     * 按时间顺序获取所有分享的分页数据
     * @param page 当前页面
     * @param size 每一页的记录数
     * @return 返回分页数据
     */
    Page<Topic> getTopicsPage(int page, int size);

    /**
     * 按热度获取所有分享的分页数据
     * @param page 当前页面
     * @param size 每一页的记录数
     * @return 返回分页数据
     */
    Page<Topic> getTopicsPageOrderByFavourite(int page, int size);

    /**
     * 通过标签获取分享分页数据.
     *
     * @param tags the tags
     * @param page 当前页号
     * @param size 每页大小
     * @return 分享的分页数据
     */
    Pagination<Topic> getTopicsPageByTags(String tag, int page, int size);

    /**
     * 通过用户UID获取所有分享.
     *
     * @param user 用户
     * @param page 页号
     * @param size 每页大小
     * @return 返回该用户的所有分享
     */
    Page<Topic> getTopicsPageByUserId(User user, int page, int size);

    /**
     * 通过分享id获取分享
     * @param topic_id 分享UID
     * @return 返回一个分享
     */
    Topic getTopicById(Integer topic_id);

    /**
     * 通过用户id和分享id获取分享
     * @param topic_id 分享UID
     * @param user 用户UID
     * @return 获取到分享返回true
     */
    boolean getTopicByUserIdAndTopicId(Integer topic_id, User user);

    /**
     * 获取分享的总数
     * @param user 登录用户
     * @return 整型值
     */
    int getTopicNumber(User user);

    /**
     * 获取动态的总数.
     *
     * @return 动态总数
     */
    long getCount();

    /**
     * 获取相应标签的动态的总数.
     *
     * @return 动态总数
     */
    long getCountByTag(String tag);
}
