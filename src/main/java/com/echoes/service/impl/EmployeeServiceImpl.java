package com.echoes.service.impl;

import com.echoes.entity.Employee;
import com.echoes.mapper.EmployeeMapper;
import com.echoes.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-24
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
