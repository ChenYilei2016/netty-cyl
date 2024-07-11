package io.github.chenyilei2016.nettycluster.redis;

import io.github.chenyilei2016.nettycluster.domain.MsgAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author chenyilei
 * @since 2024/07/11 15:10
 */
@Component
public class Publisher {

    private final RedisTemplate<String, Object> redisMessageTemplate;
    
    public Publisher(@Autowired RedisTemplate<String, Object> redisMessageTemplate) {
        this.redisMessageTemplate = redisMessageTemplate;
    }

    public void pushMessage(String topic, MsgAgreement message) {
        redisMessageTemplate.convertAndSend(topic, message);
    }

}
