package com.xia.tec.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Lee on 2018/3/26.
 */
@Controller
public class HtmlController {

    @RequestMapping("/")
    @ResponseBody
    public String sayHello(){
        return "hello world";
    }
}
