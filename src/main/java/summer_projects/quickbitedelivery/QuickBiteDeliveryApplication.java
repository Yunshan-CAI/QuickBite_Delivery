//start the mysql server
// sudo /usr/local/mysql-8.4.1-macos14-arm64/support-files/mysql.server start

package summer_projects.quickbitedelivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement

public class QuickBiteDeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuickBiteDeliveryApplication.class,args);
        log.info("项目启动成功...");
    }
}

//项目待做的事情：
//gitignore文件的内容填充一下
