
package summer_projects.quickbitedelivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching //开启spring cache注解方式的缓存功能

public class QuickBiteDeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuickBiteDeliveryApplication.class, args);
        log.info("项目启动成功...");
    }
}


//start the mysql server
//sudo /usr/local/mysql-8.4.1-macos14-arm64/support-files/mysql.server start
//启动redis(cd到redis文件夹里): redis-server redis.conf
//可以改进的地方：
//没有写的类与方法：主要是ordercontroller和usercontroller没有补全，相关的request:/order/userPage, order/page, /user/loginout...
//用户名的问题
//主从复制没有实现
//前后端分别部署



