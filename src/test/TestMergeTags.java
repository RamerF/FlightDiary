package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * 合并标签：
 *  找出文件中缺少的标签
 * @author ramer
 *
 */
public class TestMergeTags {
  @Test
  public void testMergeTags() throws Exception {
    List<String> tagsInFile = new ArrayList<>();
    List<String> tagsInDatabase = new ArrayList<>();
    List<String> updateTags = new ArrayList<>();
    tagsInDatabase.add("标签1");
    tagsInDatabase.add("标签2");
    tagsInDatabase.add("标签3");
    tagsInDatabase.add("标签4");
    tagsInDatabase.add("标签5");
    tagsInFile.add("标签4");
    tagsInFile.add("标签4");
    tagsInFile.add("标签3");
    for (int i = 0; i < tagsInDatabase.size(); i++) {
      if (!tagsInFile.contains(tagsInDatabase.get(i))) {
        updateTags.add(tagsInDatabase.get(i));
      }
    }
    for (String string : updateTags) {
      System.out.println("----" + string);
    }
  }

}
