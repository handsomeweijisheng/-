package com.wjs.takeout.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

/**  定义一个全局异常处理类
 * @author wjs
 * @createTime 2022-11-01 22:37
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHadler(SQLIntegrityConstraintViolationException ex){
        //log.info(ex.getMessage());
        if(ex.getMessage().contains("Duplicate")){
            String[] s = ex.getMessage().split(" ");
            String value=s[2];
            return Result.error(value+"已存在");
        }
        return Result.error("unknown error");
    }
}
