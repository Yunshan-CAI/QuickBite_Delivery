package summer_projects.quickbitedelivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.entity.Employee;
import summer_projects.quickbitedelivery.service.EmployeeService;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * employee login in
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //md5 encrypt the password
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //query the database by username
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //if no result then login failed
        if (emp == null) {
            return R.error("Login failed");
        }

        //compare the password, if not the same login failed
        if (!password.equals(emp.getPassword())) {
            return R.error("Login failed");
        }

        //check the employee's status, login failed if status is 0 (banned)
        if (emp.getStatus() == 0) {
            return R.error("Account is locked");
        }

        //if login succeeded, store the employee id into the session and return result
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * employee log out
     * @param request
     * @return
     */
    @PostMapping
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("Log out succeeded");
    }
}
