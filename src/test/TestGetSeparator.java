package test;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;


import java.util.Properties;
import java.util.Set;

public class TestGetSeparator {
  public static void main(String[] args) {
    Properties properties = System.getProperties();
    System.out.println(File.separatorChar);
    Set<Entry<Object, Object>> proSet = properties.entrySet();

    for (Map.Entry<Object, Object> mEntry : proSet) {
      Object value = mEntry.getValue();
      Object key = mEntry.getKey();
      System.out.println("[" + key + "=" + value + "]");
    }

  }

}
