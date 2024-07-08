package io.github.chenyilei2016.netty_basic.websocket_with_springboot.domain;

public class ClientMsgProtocol {

    private int type;       //1请求个人信息，2发送聊天信息
    private String msgInfo; //消息

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsgInfo() {
        return msgInfo;
    }

    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }
}

