package summer_projects.quickbitedelivery.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.entity.Employee;
import summer_projects.quickbitedelivery.mapper.EmployeeMapper;
import summer_projects.quickbitedelivery.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Autowired
    private EmployeeService employeeService;

    @Override
    public Employee logIn(HttpServletRequest request, Employee employee) {


        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        return emp;
    }

    @Override
    public void save(HttpServletRequest request, Employee employee) {
        //get the id of the logged user
        //Long empId = (long) request.getSession().getAttribute("employee");

        //手动设置createUser和updateUser
        employee.setCreateUser(1L);
        employee.setUpdateUser(1L);

        employeeService.save(employee);
    }

    @Override
    public Page page(int page, int pageSize, String name) {
        //page constructor
        Page pageInfo = new Page(page, pageSize);

        //condition constructor
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();

        //filter condition
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);

        //order condition
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);

        //execute the query
        employeeService.page(pageInfo, lambdaQueryWrapper);
        return pageInfo;
    }

    @Override
    public void update(HttpServletRequest request, Employee employee) {
        /*        Object empId = request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((long) empId);*/
        //手动设置update_user
        employee.setUpdateUser(1L);
        employeeService.updateById(employee);

    }
}
