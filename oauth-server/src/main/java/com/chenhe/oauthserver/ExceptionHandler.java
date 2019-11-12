package com.chenhe.oauthserver;

import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chenhe
 * @date 2019-11-05 10:19
 * @desc
 */
@ControllerAdvice
@Order(1)
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public String customGenericExceptionHnadler(AccessDeniedException exception) { //还可以声明接收其他任意参数
        //exception.printStackTrace();
        return "😀😀😀<br>对不起,无权限访问!";
    }
}
