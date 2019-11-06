package com.chenhe.oauthserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenhe
 * @date 2019-11-06 17:25
 * @desc
 */

@RestController
@RequestMapping("/test")
public class TestController {
    /**
     * 需要用户角色权限
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String helloUser() {
        return "hello tester";
    }
}
