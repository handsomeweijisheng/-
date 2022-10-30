package com.wjs.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.wjs.takeout.common.Result;
import com.wjs.takeout.entity.Employee;
import com.wjs.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wjs
 * @createTime 2022-09-04 21:39
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request http请求
     * @param employee 员工实体
     * @return 返回值
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //System.out.println("进入到登陆方法");
        //1.将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employee1 = employeeService.getOne(queryWrapper);
        if(employee1==null){
            Result.error("该用户不存在");
        }
        //3.密码比对,如果不一致返回登陆失败结果
        assert employee1 != null;
        if(employee1.getPassword().equals(password)){
        }else{
            return Result.error("用户名或密码输入错误");
        }
        //4.查看员工状态,如果为已禁用状态,返回员工已禁用结果
        if(0==employee1.getStatus()){
            //System.out.println(employee1.getStatus());
            return Result.error("该员工账号已经被禁用");
        }
        request.getSession().setAttribute("employee",employee1);
        return Result.success(employee1);
    }

    /**
     *
     * @return 用户退出登录
     */
    @RequestMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        //log.info("用户退出登录");
        request.getSession().removeAttribute("employee");
        return Result.success("用户退出登录成功");
    }
}
