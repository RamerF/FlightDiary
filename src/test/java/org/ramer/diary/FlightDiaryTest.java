package org.ramer.diary;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.service.TopicService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.EncryptUtil;
import org.ramer.diary.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目测试类
 * @author ramer
 *
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FlightDiaryTest{

    @Autowired
    private TopicService topicService;

    /**
     * Test1.
     */
    @Test
    public void testSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = simpleDateFormat.format(new Date());
        log.debug(date);
    }

    /**
     * 测试加密
     */
    @Test
    public void testEncrypt() {
        String string = EncryptUtil.execEncrypt("Jelly");
        log.debug("string: {}", string);
    }

    /**
     * 测试邮箱正则表达式
     */
    @SuppressWarnings("resource")
    @Test
    public void testRegex() {
        String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Scanner scanner = new Scanner(System.in);
        String match = null;
        match = scanner.next();
        while (!match.matches(regex)) {
            log.debug("string = " + match);
            match = scanner.next();
        }
        log.debug("true");
    }

    /**
     * 测试过期时间
     */
    @Test
    public void testExpireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        log.debug("{}", calendar.get(Calendar.MINUTE));
    }

    /**
     * 测试页面常量
     */
    @Test
    public void testPageConstant() {
        log.debug(PageConstant.HOME);
    }

    @Autowired
    private UserService userService;

    /**
     * 测试获取达人:  发表最多分享的用户
     */
    @Test
    public void testGetTopPeople() {
        Pagination<User> pageUser = userService.getTopPeople(1, 3);
        List<User> users = pageUser.getContent();
        log.debug("当前第 " + pageUser.getNumber() + "页");
        for (User user : users) {
            log.debug("username : " + user.getUsername());
        }
    }

    /**
     * 测试通过标签和页码获取分享
     */
    @Test
    public void testGetTopicPageByTags() {
        Pagination<Topic> pageTopic = topicService.getTopicsPageByTags("重庆", 2, 4);
        List<Topic> topics = pageTopic.getContent();
        for (Topic topic : topics) {
            log.debug("username : " + topic.getContent());
        }
    }

    /**
     * 获取所有标签.
     */
    @Test
    public void testGetAllTags() {
        List<String> tags = topicService.getAllTags();
        log.debug("标签名 :  ");
        for (String tag : tags) {
            log.debug("\t" + tag);
        }
        log.debug("----------------------\n" + "第一个标签 :  " + tags.iterator().next());
    }

    /**
     * ;分割标签.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSplitTags() throws Exception {
        String tag1 = "纪实";
        String tag2 = "纪实,旅游";
        List<String> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : tags) {
            stringBuilder.append(string + ",");
        }
        String[] strings = stringBuilder.toString().split(",");
        List<String> tagslist = Arrays.asList(strings);
        tagslist = new ArrayList<>(tagslist);
        log.debug("list---------------------" + tagslist);
        for (int i = 0; i < tagslist.size(); i++) {
            for (int j = i + 1; j < tagslist.size(); j++) {
                if (tagslist.get(i).equals(tagslist.get(j))) {
                    tagslist.remove(j);
                    j--;
                }
            }
        }
        for (String string : tagslist) {
            log.debug("-------------------" + string);
        }

    }

    /**
     * 去除前后的;.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSplitTags2() throws Exception {
        String tagStr = ";UI;";
        tagStr = tagStr.startsWith(";") ? tagStr.substring(1) : tagStr;
        tagStr = tagStr.endsWith(";") ? tagStr.substring(0, tagStr.length() - 1) : tagStr;
        log.debug(tagStr);
    }

    /**
     * 测试特殊参数获取分页数据.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetPageByUserIdOrderByDateDesc() throws Exception {
        User user = new User();
        user.setId(1);
        Page<Topic> topics = topicService.getTopicsPageByUserId(user, 2, 10);
        for (Topic topic : topics.getContent()) {
            log.debug("{}", topic.getId());
        }
    }

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() throws Exception {
        String pass = null;
        if (redisTemplate.opsForHash().get("ramer", "id") == null) {
            redisTemplate.opsForHash().put("ramer", "id", "ramer");
        }
        pass = (String) redisTemplate.opsForHash().get("ramer", "id");
        Assert.assertEquals("ramer", redisTemplate.opsForHash().get("ramer", "id"));
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " pass : {}", pass);
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " pass : {}",
                stringRedisTemplate.opsForHash().get("ramer", "id"));
    }
}
