package io.github.chenyilei2016.netty_basic.传输文件.domain;

import lombok.Data;

@Data
public class FileTransferProtocol {

    private Integer transferType; //0请求传输文件、1文件传输指令、2文件传输数据
    private Object transferObj;   //数据对象；(0)FileDescInfo、(1)FileBurstInstruct、(2)FileBurstData

}
