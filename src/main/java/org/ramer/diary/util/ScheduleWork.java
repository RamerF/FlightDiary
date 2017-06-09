package org.ramer.diary.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.ramer.diary.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * schedule job:
 *  execute  every ten seconds;
 *  query tags from table topic and remove duplicates ,and compare to local file "/xml/tags.xml";
 *  new tags will be append to file.
 *
 * 定时任务：
 *  定时获取数据库中的tag，并比对xml/tags.xml文件中的tag如果有新的tag就写入到文件中
 * @author ramer
 *
 */
@Slf4j
public class ScheduleWork implements ApplicationListener<ContextRefreshedEvent>{

    /**
     * get millis for 'time'
     * @param time "HH:mm:ss"
     * @return
     */
    private static long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Autowired
    TopicService topicService;
    @Value("${diary.tags.xml.position}")
    private String files;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            // execute every ten seconds
            //      long oneDay = 30 * 60 * 1000;
            long oneDay = 300 * 1000;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            // start now
            long initDelay = getTimeMillis(simpleDateFormat.format(new Date())) - System.currentTimeMillis();
            initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;
            // start execute job
            executor.scheduleAtFixedRate(() -> {
                log.debug("----------start update tags-----------");
                // tags in database
                String file = System.getProperty("flightdiary.root") + files;
                log.debug(file);
                List<String> tags = topicService.getAllTags();
                //    去除重复的标签
                StringBuilder stringBuilder = new StringBuilder();
                for (String string : tags) {
                    stringBuilder.append(string + ";");
                }
                tags = CollectionsUtils.removeSame(Arrays.asList(stringBuilder.toString().split(";")));

                log.debug("tags in database： ");
                for (String string : tags) {
                    log.debug("\t" + string);
                }
                Set<String> tagsInFile = new HashSet<>();
                try {
                    // read tags in local file
                    tagsInFile = FileUtils.readTag(file);
                } catch (Exception e) {
                    log.debug("Exception ScheduleWork(Line 111)");
                    e.printStackTrace();
                }
                log.debug("tags in file： ");
                for (String string : tagsInFile) {
                    log.debug("\t" + string);
                }
                //将要添加的tag
                List<String> updateTags = new ArrayList<>();
                // less than tags in database ,so each the tags in local file
                for (int i = 0; i < tags.size(); i++) {
                    if (!tagsInFile.contains(tags.get(i))) {
                        updateTags.add(tags.get(i));
                    }
                }
                log.debug("update tags： ");
                for (String string : updateTags) {
                    log.debug("\t" + string);
                }
                if (updateTags.size() == 0) {
                    log.debug("没有要添加的标签");
                    return;
                }
                try {
                    log.debug("更新文件");
                    // update tags in local file
                    FileUtils.writeTag(updateTags, file);
                } catch (Exception e) {
                    log.debug("Exception ScheduleWork(Line 129)");
                    e.printStackTrace();
                }

            }, initDelay, oneDay, TimeUnit.MILLISECONDS);
        }
    }

}