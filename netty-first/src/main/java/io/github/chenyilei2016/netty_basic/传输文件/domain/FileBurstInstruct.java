package io.github.chenyilei2016.netty_basic.传输文件.domain;

import lombok.Data;

//文件分片指令
@Data
public class FileBurstInstruct {
    private Integer status;       //Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
    private String clientFileUrl; //客户端文件URL
    private Integer readPosition; //读取位置
}
