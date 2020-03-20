package com.chenhe;

import org.dromara.soul.client.common.annotation.SoulClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenhe
 * @date 2020-01-12 18:34
 * @desc
 */
@SoulClient(path="/index",desc="首页地址")
@RestController
public class IndexController {

    @RequestMapping("index")
    public String index(){
        return "成功访问通了!";
    }
}
