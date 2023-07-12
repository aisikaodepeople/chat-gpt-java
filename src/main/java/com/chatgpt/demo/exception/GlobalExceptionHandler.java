package com.chatgpt.demo.exception;

import com.chatgpt.demo.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.fail(e.getMessage());
    }

}