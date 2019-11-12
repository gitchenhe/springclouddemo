package com.chenhe.oauthserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenhe
 * @date 2019-11-08 09:58
 * @desc
 */
@Controller
@RequestMapping("auth")
public class TestController {

    @RequestMapping("doauth")
    public String doAuth(){

        return "doauth";
    }

    @RequestMapping("code")
    public String getCode(@RequestParam(value = "code",required = false) String code, Model model) {
        if (StringUtils.isEmpty(code)){
            model.addAttribute("code",code);
        }
        return "callback";
    }
}
