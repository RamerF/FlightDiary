package org.ramer.diary.service;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;

import java.util.List;

/**
 * Created by RAMER on 5/17/2017.
 */
public interface PraiseService {
    /**
     * 获取已点赞分享的id
     * @param user 登录用户
     * @param other 当前浏览的用户
     * @return 返回所有已点赞且,被浏览用户分享的,分享id的集合
     */
    List<Integer> getPraiseTopicIds(User user, User other);

    /**
     * 用户点赞
     * @param user 用户
     * @param topic 分享
     * @return 用户点赞成功返回true
     */
    boolean praise(User user, Topic topic);

    /**
     * 取消点赞
     * @param topic 分享
     * @param user  用户
     * @return 取消成功返回true
     */
    boolean notPraise(Topic topic, User user);
}
