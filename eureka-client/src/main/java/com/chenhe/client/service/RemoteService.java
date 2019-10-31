package com.chenhe.client.service;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author chenhe
 * @date 2019-07-09 11:37
 * @desc
 */
//@FeignClient("provider")
public interface RemoteService {
    @GetMapping("/api/time")
    String timestamp();
}
