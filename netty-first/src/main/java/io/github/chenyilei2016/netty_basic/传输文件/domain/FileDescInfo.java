package io.github.chenyilei2016.netty_basic.传输文件.domain;

import lombok.Data;

//文件传输信息
@Data
public class FileDescInfo {
    private String fileUrl;
    private String fileName;
    private Long fileSize;

}
