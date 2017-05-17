package org.ramer.diary;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * 获取系统路径分隔符和类路径
 * @author ramer
 *
 */
@Slf4j
public class TestGetSeparator{
    @Test
    public void testGetSeparator() {
        Properties properties = System.getProperties();
        log.debug("{}", File.separatorChar);
        Set<Entry<Object, Object>> proSet = properties.entrySet();

        for (Map.Entry<Object, Object> mEntry : proSet) {
            Object value = mEntry.getValue();
            Object key = mEntry.getKey();
            log.debug("[" + key + "=" + value + "]");
        }
    }

    @Test
    //    获取类路径
    public void getPath() throws IOException {
        String path = new File("").getCanonicalPath();
        log.debug("path = " + path);
        log.debug("{}", System.getProperty("os.name").equals("Linux"));

    }

}
