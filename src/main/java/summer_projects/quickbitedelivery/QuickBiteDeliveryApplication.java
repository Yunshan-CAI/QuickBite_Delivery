package summer_projects.quickbitedelivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import summer_projects.quickbitedelivery.controller.EmployeeController;

@Slf4j
@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ComponentScan("summer_projects.quickbitedelivery.controller.EmployeeController")
public class QuickBiteDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickBiteDeliveryApplication.class, args);
        log.info("QuickBiteDeliveryApplication started");
    }
}
