package io.pivotal.poc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
@Slf4j
public class RedisSessionsApplication implements CommandLineRunner {


    @Autowired
    RedisOperationsSessionRepository sessions;

    private ObjectMapper objectMapper = new ObjectMapper();


    public static void main(String[] args) {
        SpringApplication.run(RedisSessionsApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

    }

    @Controller
    public class ShowSessionController {
        @RequestMapping("/clearSession")
        /**
         * This does not clear session from redis, just invalidated user session.
         * Sessions will be purched from Redis after timeout defined by maxInactiveIntervalInSeconds.
         */
        public ModelAndView clearSession(HttpSession session) {
            ModelAndView mav = new ModelAndView("deleted");
            mav.addObject("message", String.format("Attempted to delete session - %s ", session.getId()));
            sessions.delete(session.getId());
            return mav;
        }

        @RequestMapping({"/"})
        String index(HttpSession session) {
            session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, "user-name");
            return "index";
        }


        @ModelAttribute("sessionData")
        public Map<String, String> messages() throws JsonProcessingException {
            Map<String, ? extends Session> byIndexNameAndIndexValue = sessions.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, "user-name");
            Map<String, String> renderedSession = new HashMap<>();

            for (Map.Entry<String, ? extends Session> entry : byIndexNameAndIndexValue.entrySet()) {
                renderedSession.put(entry.getKey(), objectMapper.writeValueAsString(renderEntryValue(entry)));
            }

            return renderedSession;
        }

        private Map<String, String> renderEntryValue(Map.Entry<String, ? extends Session> entry) {
            Map<String, String> renderedValue = new HashMap<>();
            entry.getValue().getAttributeNames().stream()
                    .forEach(attrKey -> renderedValue.put(attrKey, entry.getValue().getAttribute(attrKey)));
            return renderedValue;
        }


    }
}
