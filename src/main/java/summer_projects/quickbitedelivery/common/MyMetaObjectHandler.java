package summer_projects.quickbitedelivery.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insertfill automatically");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        //为了移动端添加地址要把这里暂时注释掉
  //      metaObject.setValue("createUser", BaseContext.getCurrentId());
//        metaObject.setValue("updateUser", BaseContext.getCurrentId());
        //为了防止insertFill覆盖我手动设置的create_user值
        Object createUser = metaObject.getValue("createUser");
        if (createUser == null) {
            metaObject.setValue("createUser", BaseContext.getCurrentId());
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("updatefill automatically");
        log.info(metaObject.toString());

        metaObject.setValue("updateTime", LocalDateTime.now());

       //为了防止updateFill覆盖我手动设置的update_user值
        Object updateUser = metaObject.getValue("updateUser");
        if (updateUser == null) {
            metaObject.setValue("updateUser", BaseContext.getCurrentId());
        }

        //check the current thread
        long id = Thread.currentThread().getId();
        log.info("current thread is {}", id);
    }
}
