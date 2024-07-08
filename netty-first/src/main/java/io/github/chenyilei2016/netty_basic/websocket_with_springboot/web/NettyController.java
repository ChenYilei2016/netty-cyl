package io.github.chenyilei2016.netty_basic.websocket_with_springboot.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class NettyController {

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("name", "xiaohao");
        return "index";
    }

}
