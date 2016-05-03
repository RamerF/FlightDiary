package org.ramer.diary.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.ramer.diary.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

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
public class ScheduleWork implements ApplicationListener<ContextRefreshedEvent> {

  /**
   * get millis for 'time'
   * @param time "HH:mm:ss"
   * @return
   */
  private long getTimeMillis(String time) {
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
  @Value("#{diaryProperties['tags.xml.position']}")
  private String file;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    if (contextRefreshedEvent.getApplicationContext().getParent() == null) {

      ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
      // execute every ten seconds
      long oneDay = 5 * 60 * 1000;

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
      // start now
      long initDelay = getTimeMillis(simpleDateFormat.format(new Date()))
          - System.currentTimeMillis();
      initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;
      // start execute job
      executor.scheduleAtFixedRate(() -> {
        System.out.println("----------start update tags-----------");
        // tags in database
        System.out.println(topicService);
        System.out.println(file);
        List<String> tags = topicService.getAllTags();
        // remove duplicate
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : tags) {
          stringBuilder.append(string + ";");
        }
        String[] strings = stringBuilder.toString().split(";");
        // Arrays.asList will return a proxy with doesn't implement add() and remove(),so create a list.
        List<String> tagslist = Arrays.asList(strings);
        tagslist = new ArrayList<>(tagslist);
        for (int i = 0; i < tagslist.size(); i++) {
          for (int j = i + 1; j < tagslist.size(); j++) {
            if (tagslist.get(i).equals(tagslist.get(j))) {
              tagslist.remove(j);
              j--;
            }
          }
        }
        // tags no duplicate.
        tags = tagslist;
        System.out.println("tags in database： ");
        for (String string : tags) {
          System.out.println("\t" + string);
        }
        List<String> tagsInFile = new ArrayList<>();
        try {
          // read tags in local file
          tagsInFile = FileUtils.readTag(file);
        } catch (Exception e) {
          System.out.println("Exception ScheduleWork(Line 111)");
          e.printStackTrace();
        }
        System.out.println("tags in file： ");
        for (String string : tagsInFile) {
          System.out.println("\t" + string);
        }
        //将要添加的tag
        List<String> updateTags = new ArrayList<>();
        // less than tags in database ,so each the tags in local file
        for (int i = 0; i < tags.size(); i++) {
          if (!tagsInFile.contains(tags.get(i))) {
            updateTags.add(tags.get(i));
          }
        }
        System.out.println("update tags： ");
        for (String string : updateTags) {
          System.out.println("\t" + string);
        }
        if (updateTags.size() == 0) {
          System.out.println("没有要添加的标签");
          return;
        } else {
          try {
            System.out.println("更新文件");
            // update tags in local file
            FileUtils.writeTag(updateTags, file);
          } catch (Exception e) {
            System.out.println("Exception ScheduleWork(Line 129)");
            e.printStackTrace();
          }
        }

      }, initDelay, oneDay, TimeUnit.MILLISECONDS);
    }
  }

}