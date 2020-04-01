package com.chenhe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenhe
 * @date 2020-03-24 16:14
 * @desc
 */
@RestController
public class TestController {

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("index.html").forward(request,response);
    }
    @RequestMapping("p1")
    public String p1(){
        return "p1";
    }


    @RequestMapping("p2")
    public String p2(){
        return "p2";
    }


    @RequestMapping("p3")
    public String p3(){
        return "p3";
    }

    @RequestMapping("callback")
    public String callback(String code){
        return code;
    }

    @RequestMapping("userInfo")
    public String userInfo(){
        return "userInfo";
    }
}
