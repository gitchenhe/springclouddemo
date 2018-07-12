package com.chenhe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenhe
 * @Date 2018-07-11 14:18
 * @desc
 **/
@RestController
public class DescController {
    @RequestMapping("info")
    @ResponseBody
    public String info() {
        return "配置中心";
    }
}
