package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 注销.
 * @author ramer
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class LogOff{

    /**
     * 注销用户;.
     *
     * @param session the session
     * @param map the map
     * @return 主页;
     */
    @GetMapping("/logOff")
    public String logOff(HttpSession session, Map<String, Object> map) {
        log.debug("注销");
        map.clear();
        session.invalidate();
        return "redirect:/home";
    }

}