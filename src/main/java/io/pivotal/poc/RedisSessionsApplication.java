package io.pivotal.poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableRedisHttpSession
@Slf4j
public class RedisSessionsApplication implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    String mySessionAttribute = "mySessionAttribute";

    public static void main(String[] args) {
        SpringApplication.run(RedisSessionsApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
    }

    @Controller
    public class ShowSessionController {
        @RequestMapping("/clearSession")
        public String clearSession(HttpSession session) {
            redisTemplate.delete(sessionKeys());
            return index(session);
        }

        @RequestMapping({"/"})
        String index(HttpSession session) {
            session.setAttribute("sessionKeys", sessionKeys());
            return "index";
        }

        private List<String> sessionKeys() {
            Set<String> keys = redisTemplate.keys("*");
            List<String> keysFromSession = new ArrayList<>();
            for (String next : keys) {
                keysFromSession.add(next);
            }
            return keysFromSession;
        }
    }

    @Bean
    RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(connectionFactory);

        return template;
    }

}
