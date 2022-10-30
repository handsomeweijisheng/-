package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.entity.Employee;
import com.wjs.takeout.mapper.EmployeeMapper;
import com.wjs.takeout.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author wjs
 * @create 2022-09-04 21:34
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
