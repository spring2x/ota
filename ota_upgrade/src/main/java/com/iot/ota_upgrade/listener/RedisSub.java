package com.iot.ota_upgrade.listener;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * redis消息订阅
 * @author tang
 *
 */
@Configuration
public class RedisSub {

	@Value("${redis.pubSub.database0.channel}")
	private String redisDataBase0Channel;
	
	
	@Autowired
	private RedisKeyExpiredListener redisKeyExpiredListener;

	// 初始化监听器
	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		// container.addMessageListener(listenerAdapter, new
		// PatternTopic(channel));
		//监听所有的redis topic
		Collection<Topic> topics = new ArrayList<>();
		topics.add(new PatternTopic(redisDataBase0Channel));
		container.addMessageListener(listenerAdapter, topics);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter() {
		return new MessageListenerAdapter(redisKeyExpiredListener);
	}
	
	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}
}
