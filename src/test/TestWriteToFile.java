package test;

import org.ramer.diary.util.FileUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/*
 * 读取文件内容并比对数据库中的tags查看是否有新tag，如果有追加内容到文件
 * @author ramer
 *
 */
//@ContextConfiguration("classpath:applicationContext.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
public class TestWriteToFile {

  /**
   * 读取文件中的tag
   *
   * @throws Exception the exception
   */
  @Test
  public void testReadTags() throws Exception {
    URL url = FileUtils.class.getClassLoader().getResource("tags.xml");
    Set<String> tagsList = FileUtils.readTag(url.getFile());
    System.out.println("文件中已有标签： ");
    for (String tag : tagsList) {
      System.out.print("\t" + tag);
    }
  }

  @Test
  public void testWriteTags() throws Exception {
    URL url = FileUtils.class.getClassLoader().getResource("tags.xml");
    List<String> tags = new ArrayList<>();
    tags.add("测试1");
    tags.add("测试2");
    FileUtils.writeTag(tags, url.getFile());
  }

}
