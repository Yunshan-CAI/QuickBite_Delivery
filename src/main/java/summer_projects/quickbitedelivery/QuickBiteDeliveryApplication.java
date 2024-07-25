//start the server
// sudo /usr/local/mysql-8.4.1-macos14-arm64/support-files/mysql.server start


package summer_projects.quickbitedelivery;

//import lombok.extern.slf4j.Slf4j;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.context.annotation.ComponentScan;
//import summer_projects.quickbitedelivery.controller.EmployeeController;
//
//@Slf4j
//@SpringBootApplication
////@ComponentScan(basePackages = "summer_projects.quickbitedelivery")
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
////@ComponentScan("summer_projects.quickbitedelivery.mapper.EmployeeMapper")
//@ComponentScan("summer_projects.quickbitedelivery.mapper")
////@ComponentScan("summer_projects.quickbitedelivery.controller.EmployeeController")
//
//public class QuickBiteDeliveryApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(QuickBiteDeliveryApplication.class, args);
//        log.info("QuickBiteDeliveryApplication started");
//    }
//}

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class QuickBiteDeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuickBiteDeliveryApplication.class,args);
        log.info("项目启动成功...");
    }
}
