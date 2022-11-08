package com.wjs.takeout.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**  定义一个全局异常处理类
 * @author wjs
 * @createTime 2022-11-01 22:37
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     *  定义异常处理类处理主键重复问题
     * @param ex 异常
     * @return 返回值
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //log.info(ex.getMessage());
        if(ex.getMessage().contains("Duplicate")){
            String[] s = ex.getMessage().split(" ");
            String value=s[2];
            return Result.error(value+"已存在");
        }
        return Result.error("unknown error");
    }

    /**
     *  定义异常返回值处理删除分类问题
     * @param exception 异常
     * @return 返回值
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(CustomException exception){
        return Result.error(exception.getMessage());
    }
    /**
     *  定义异常返回值处理删除分类问题
     * @param exception 异常
     * @return 返回值 I
     */
    @ExceptionHandler(FileNotFoundException.class)
    public void exceptionHandler(FileNotFoundException exception){
        //return Result.error(exception.getMessage());
        //System.out.println(exception.getMessage());
        log.info("{}",exception.getMessage());
    }
}
