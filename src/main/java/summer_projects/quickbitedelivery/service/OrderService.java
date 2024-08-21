package summer_projects.quickbitedelivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import summer_projects.quickbitedelivery.entity.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders order);
}
