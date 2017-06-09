package org.ramer.diary.service;

import org.ramer.diary.domain.Reply;

/**
 * Created by RAMER on 5/17/2017.
 */
public interface ReplyService {
    /**
     * 回复评论.
     *
     * @param reply 回复类
     * @return 回复成功返回true
     */
    boolean replyComment(Reply reply);

    /**
     * 回复一个回复
     *
     * @param reply 回复
     */
    void replyReply(Reply reply);

    /**
     * 删除一个回复
     * @param id 回复ID
     * @return 操作成功返回true
     */
    boolean deleteReply(Integer id);
}
