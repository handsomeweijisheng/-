package com.wjs.takeout.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjs
 * @create 2022-09-04 21:00
 */
@RestController
@RequestMapping("/employee")
public class TestController {
    @RequestMapping("/nice")
    public String t1(){
        return "nice to mybatisPlus";
    }
}
