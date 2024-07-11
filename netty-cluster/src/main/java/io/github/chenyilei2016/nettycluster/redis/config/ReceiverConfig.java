package io.github.chenyilei2016.nettycluster.redis.config;

import io.github.chenyilei2016.nettycluster.redis.MsgDispatchChannelReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


@Configuration
public class ReceiverConfig {

    @Bean
    public MessageListenerAdapter msgAgreementListenerAdapter(MsgDispatchChannelReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }


    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter msgAgreementListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(msgAgreementListenerAdapter, new PatternTopic("itstack-demo-netty-push-msgAgreement"));
        return container;
    }



}
