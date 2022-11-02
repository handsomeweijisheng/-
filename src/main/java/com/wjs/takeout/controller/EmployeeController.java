package com.wjs.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.takeout.common.Result;
import com.wjs.takeout.entity.Employee;
import com.wjs.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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
    @PostMapping
    public Result<String> addEmployee(@RequestBody Employee employee,HttpServletRequest request){
        log.info("进入员工信息添加界面,添加员工");
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Employee employee1 = (Employee) request.getSession().getAttribute("employee");
        employee.setCreateUser(employee1.getId());
        employee.setUpdateUser(employee1.getId());
        employee.setStatus(1);
        employeeService.save(employee);
        log.info("员工添加成功");
        return Result.success("员工添加成功");
    }
    @GetMapping("/page")
    public Result<Page<Employee>> splitPage(int page,int pageSize,String name){
        //log.info("page:{},pageSize:{},name:{}",page,pageSize,name);
        //1.构造分页构造器
        Page<Employee> page1 = new Page<>(page, pageSize);
        //2.构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeLeft(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(page1,queryWrapper);
        return Result.success(page1);
    }
    @PutMapping
    public Result<String> updateEmployee(@RequestBody Employee employee,HttpServletRequest request){
        //log.info("{}",employee);
        employee.setUpdateTime(LocalDateTime.now());
        Employee employee1 = (Employee) request.getSession().getAttribute("employee");
        employee.setUpdateUser(employee1.getId());
        employeeService.updateById(employee);
        return Result.success("1");
    }
}
