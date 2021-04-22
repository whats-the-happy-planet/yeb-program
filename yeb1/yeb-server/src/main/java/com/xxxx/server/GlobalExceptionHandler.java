package com.xxxx.server;

import com.xxxx.server.exceptions.ParamsException;
import com.xxxx.server.pojo.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获全局异常
     */
    @ExceptionHandler(value = Exception.class)
    Object handleException(Exception e, HttpServletRequest request) {

        RespBean respBean = new RespBean();
        respBean.setCode(500);
        respBean.setMessage(e.getMessage());
        respBean.setObj(request.getRequestURL());
        //控制台打印异常信息
        e.printStackTrace();

        return respBean;
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(value = ParamsException.class)
    Object handleMyException(ParamsException p, HttpServletRequest request) {

        RespBean respBean = new RespBean();
        respBean.setCode(p.getCode());
        respBean.setMessage(p.getMessage());
        respBean.setObj(request.getRequestURL());

        return respBean;
    }
    

}