/*
 *
 */
package org.ramer.diary.controller.user;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.service.FavouriteService;
import org.ramer.diary.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 收藏和取消收藏.
 * @author ramer
 *
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class FavouriteController{
    @Resource
    private FavouriteService favouriteService;

    /**
     *
     * 收藏.
     *
     * @param topic_id 分享id
     * @param user 用户
     * @param response the response
     * @param session the session
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @PutMapping("/user/topic/favourite/{topic_id}")
    public void favourite(@PathVariable("topic_id") Integer topic_id, User user, HttpServletResponse response,
            HttpSession session) throws IOException {
        response.setCharacterEncoding("UTF-8");
        if (!UserUtils.checkLogin()) {
            response.getWriter().write("麻麻说没登录不能收藏哒 !");
            return;
        }
        Topic topic = new Topic();
        topic.setId(topic_id);
        boolean flag = favouriteService.favourite(user, topic);
        if (flag) {
            response.getWriter().write("收藏成功 !");
            return;
        }
        response.getWriter().write("你已经收藏过了哒 !");
    }

    /**
     * 取消收藏.
     *
     * @param topic_id 分享ID
     * @param user 登录用户
     * @param response JSP内置对象
     * @throws IOException 写入信息失败抛出IO异常
     */
    @DeleteMapping("/user/topic/notFavourite/{topic_id}")
    public void notFavourite(@PathVariable("topic_id") Integer topic_id, User user, HttpServletResponse response)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        Topic topic = new Topic();
        topic.setId(topic_id);
        boolean flag = favouriteService.notFavourite(user, topic);
        if (flag) {
            response.getWriter().write("取消收藏成功 !");
            return;
        }
        response.getWriter().write("你没收藏哒 !");
    }

}