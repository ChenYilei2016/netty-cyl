package io.github.chenyilei2016.nettycluster.service;

import io.github.chenyilei2016.nettycluster.domain.MsgAgreement;
import io.github.chenyilei2016.nettycluster.redis.Publisher;
import io.github.chenyilei2016.nettycluster.redis.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 扩展服务

 */
@Service("extServerService")
public class ExtServerService {

    @Resource
    private Publisher publisher;

    @Resource
    private RedisUtil redisUtil;

    public void push(MsgAgreement msgAgreement) {
        publisher.pushMessage("itstack-demo-netty-push-msgAgreement", msgAgreement);
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }
}
