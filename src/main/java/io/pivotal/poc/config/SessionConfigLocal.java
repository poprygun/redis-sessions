package io.pivotal.poc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
@Profile("!cloud")
public class SessionConfigLocal {

    @Bean
    public JedisConnectionFactory connectionFactory(@Value("${spring.redis.host}") String host
                                                    , @Value("${spring.redis.port}") int port) {
        JedisConnectionFactory connection = new JedisConnectionFactory();
        connection.setPort(port);
        connection.setHostName(host);
        return connection;
    }
}
