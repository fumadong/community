package com.fu.community.advice;

import com.alibaba.fastjson.JSON;
import com.fu.community.dto.ResultDTO;
import com.fu.community.exception.CustomizeErrorCode;
import com.fu.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 拦截已知的异常
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    //拦截所有异常
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model, HttpServletResponse response,
                        HttpServletRequest request) {
        //判断客户端请求类型
        String contentType = request.getContentType();
        ResultDTO resultDTO;
        if ("application/json".equals(contentType)) {
            if (e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ex) {
            }
            return null;
        } else {
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("message", "服务器冒烟了！！！！,稍后再试试吧！");
            }

            return new ModelAndView("error");
        }

    }

}
