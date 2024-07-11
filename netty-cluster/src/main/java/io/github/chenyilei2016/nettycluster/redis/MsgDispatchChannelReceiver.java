package io.github.chenyilei2016.nettycluster.redis;

import com.alibaba.fastjson.JSON;
import io.github.chenyilei2016.nettycluster.domain.MsgAgreement;
import io.github.chenyilei2016.nettycluster.util.CacheUtil;
import io.github.chenyilei2016.nettycluster.util.MsgUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 用于接收跨 channel的dispatcher消息
 */
@Service
public class MsgDispatchChannelReceiver extends AbstractReceiver {

    private Logger logger = LoggerFactory.getLogger(MsgDispatchChannelReceiver.class);

    @Override
    public void receiveMessage(Object message) {
        logger.info("接收到PUSH消息：{}", message);
        MsgAgreement msgAgreement = JSON.parseObject(message.toString(), MsgAgreement.class);
        String toChannelId = msgAgreement.getToChannelId();
        Channel channel = CacheUtil.cacheChannel.get(toChannelId);
        if (null == channel) return;
        channel.writeAndFlush(MsgUtil.obj2Json(msgAgreement));
    }

}
