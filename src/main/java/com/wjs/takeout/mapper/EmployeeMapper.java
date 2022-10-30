package com.wjs.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjs.takeout.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wjs
 * @create 2022-09-04 21:32
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
