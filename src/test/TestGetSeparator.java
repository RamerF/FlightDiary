package test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

/**
 * 获取系统路径分隔符和类路径
 * @author ramer
 *
 */
public class TestGetSeparator {
  @Test
  public void testGetSeparator() {
    Properties properties = System.getProperties();
    System.out.println(File.separatorChar);
    Set<Entry<Object, Object>> proSet = properties.entrySet();

    for (Map.Entry<Object, Object> mEntry : proSet) {
      Object value = mEntry.getValue();
      Object key = mEntry.getKey();
      System.out.println("[" + key + "=" + value + "]");
    }
  }

  @Test
  //    获取类路径
  public void getPath() throws IOException {
    String path = new File("").getCanonicalPath();
    System.out.println("path = " + path);
    System.out.println(System.getProperty("os.name").equals("Linux"));

  }

}
