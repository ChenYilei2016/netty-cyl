package io.github.chenyilei2016.nettycluster.domain;

import java.util.Date;

/**
 * 服务端信息
 */
public class ServerInfo {

    private String ip;    //IP
    private int port;     //端口
    private Date openDate;//启动时间

    public ServerInfo(String ip, int port, Date openDate) {
        this.ip = ip;
        this.port = port;
        this.openDate = openDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }
}
