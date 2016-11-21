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
    @Value("${spring.redis.host}")
    private String springRedisHost;

    @Value("${spring.redis.port:6379}")
    private int springRedisPort;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory connection = new JedisConnectionFactory();
        connection.setPort(springRedisPort);
        connection.setHostName(springRedisHost);
        return connection;
    }
}
