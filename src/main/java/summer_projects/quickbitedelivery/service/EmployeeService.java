package summer_projects.quickbitedelivery.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.entity.Employee;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
     Employee logIn(HttpServletRequest request, Employee employee);

     void save(HttpServletRequest request, Employee employee);

     Page page(int page, int pageSize, String name);

     void update(HttpServletRequest request, Employee employee);
}
