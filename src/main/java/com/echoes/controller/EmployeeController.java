package com.echoes.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.echoes.common.BaseContext;
import com.echoes.common.R;
import com.echoes.entity.Employee;
import com.echoes.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-24
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Value("${reggie.employeePassword}")
    private String init_password;

    /**
     * @Name : save
     * @description : 新增员工信息
     * @createTime : 2022/7/26 14:25
     * @param : employee
     * @param : request
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee,HttpServletRequest request){

        Long empId= (Long) request.getSession().getAttribute("employee");

        // 设置md5加密的初始密码
        String md5Password = DigestUtils.md5DigestAsHex(init_password.getBytes());
        employee.setPassword(md5Password);

        // 保存
        employeeService.save(employee);
        return R.success("新增员工成功！");
    }

    /**
     * @Name : update
     * @description : 更新员工信息
     * @createTime : 2022/7/26 17:45
     * @param : employee
     * @param : request
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request){

//        log.info(employee.toString());

        employeeService.updateById(employee);
        return R.success("员工信息修改成功！");
    }

    /**
     * @Name : getById
     * @description : 用户修改信息，返回当前要修改的员工信息
     * @createTime : 2022/7/26 17:45
     * @param : id
     * @return : com.echoes.common.R<com.echoes.entity.Employee>
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info(id.toString());

        Employee employee = employeeService.getById(id);
        if(employee.getUsername().equals("admin")){
            return R.error("禁止修改超级管理员admin用户名！");
        }
        return R.success(employee);
    }

    /**
     * @Name : login
     * @description : 用户登录
     * @createTime : 2022/7/26 14:25
     * @param : request
     * @param : employee
     * @return : com.echoes.common.R<com.echoes.entity.Employee>
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (null == emp){
            return R.error("登录失败！该用户不存在！");
        }

        if(!md5Password.equals(emp.getPassword())){
            return R.error("登录失败！密码错误！");
        }

        if (0 == emp.getStatus()){
            return R.error("登录失败！该账户已被禁用！");
        }

        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * @Name : logout
     * @description : 用户登出
     * @createTime : 2022/7/26 14:26
     * @param : request
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

        request.getSession().removeAttribute("employee");

        return R.success("退出成功！");
    }

    /**
     * @Name : page
     * @description : 员工分页查询
     * @createTime : 2022/7/26 14:27
     * @param : page
     * @param : pageSize
     * @param : name
     * @return : com.echoes.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
//        log.info(page+" "+pageSize+name+" ");

        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

}

