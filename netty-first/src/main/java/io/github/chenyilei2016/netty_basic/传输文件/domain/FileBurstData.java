package io.github.chenyilei2016.netty_basic.传输文件.domain;

import lombok.Data;

//文件分片数据块
@Data
public class FileBurstData {
    private String fileUrl;     //客户端文件地址
    private String fileName;    //文件名称
    private Integer beginPos;   //开始位置
    private Integer endPos;     //结束位置
    private byte[] bytes;       //文件字节；再实际应用中可以使用非对称加密，以保证传输信息安全
    private Integer status;     //Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝

}
