package org.ramer.diary.service;

import org.ramer.diary.domain.FeedBack;
import org.ramer.diary.domain.User;
import org.ramer.diary.util.Pagination;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by RAMER on 5/17/2017.
 */
public interface UserService{
    /**
     * 获取用户,按分享的数量排序
     * @param page 当前页号
     * @param 每页记录数
     * @return 用户的分页记录
     */
    Pagination<User> getTopPeople(int page, int size);

    /**
     * 用户登录
     * @param user 用户
     * @return 返回登录用户信息
     */
    User login(User user);

    /**
     * 注册或更新用户
     * @param user 用户
     * @return 返回注册成功的用户信息或已更新的用户信息
     */
    User newOrUpdate(User user);

    /**
     * 通过UID获取用户
     * @param id 用户UID
     * @return 返回一个用户
     */
    User getById(Integer id);

    /**
     * 通过用户名获取用户
     * @param username 用户名或邮箱
     * @return 返回一个用户
     */
    User getByName(String username);

    /**
     * 更新用户头像
     * @param user 要更新的用户
     */
    void updateHead(User user);

    /**
     * 通过邮箱获取用户
     *
     * @param email the email
     * @return the by email
     */
    User getByEmail(String email);

    /**
     * 用户反馈.
     *
     * @param feedBack 反馈信息
     */
    boolean feedback(FeedBack feedBack);
}
