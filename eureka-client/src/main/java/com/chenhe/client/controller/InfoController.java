package com.chenhe.client.controller;

import com.chenhe.client.service.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenhe
 * @date 2019-07-09 11:39
 * @desc
 */
@RestController
public class InfoController {
    @Autowired
    RemoteService remoteService;

    @RequestMapping("info")
    public Object info(){
        return remoteService.timestamp();
    }
}
