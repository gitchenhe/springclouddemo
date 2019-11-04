package com.chenhe.api;

import com.chenhe.routeconfig.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenhe
 * @date 2019-11-01 13:28
 * @desc
 */
@RestController
@RequestMapping("manage")
public class ApiController {
    @Autowired
    GatewayService gatewayService;


    @RequestMapping("clearRoute/{id}")
    public String clearRoute(@PathVariable String id){
        gatewayService.deleteRoute(id);
        return "OK";
    }

    @RequestMapping("saveRoute")
    public String saveRoute(String id){
        gatewayService.save();
        return "OK";
    }
}
