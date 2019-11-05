package com.chenhe.oauthserver;

import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author chenhe
 * @date 2019-11-05 10:19
 * @desc
 */
@ControllerAdvice
@Order(1)
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public void customGenericExceptionHnadler(AccessDeniedException exception) { //还可以声明接收其他任意参数
        exception.printStackTrace();
    }
}
