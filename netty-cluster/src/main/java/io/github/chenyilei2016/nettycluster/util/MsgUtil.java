package io.github.chenyilei2016.nettycluster.util;


import com.alibaba.fastjson.JSON;
import io.github.chenyilei2016.nettycluster.domain.MsgAgreement;
public class MsgUtil {

    public static MsgAgreement buildMsg(String channelId, String content) {
        return new MsgAgreement(channelId, content);
    }

    public static MsgAgreement json2Obj(String objJsonStr) {
        return JSON.parseObject(objJsonStr, MsgAgreement.class);
    }

    public static String obj2Json(MsgAgreement msgAgreement) {
        return JSON.toJSONString(msgAgreement) + "\r\n";
    }

}
