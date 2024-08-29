package summer_projects.quickbitedelivery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.entity.Orders;
import summer_projects.quickbitedelivery.service.OrderService;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders order) {
        orderService.submit(order);
        return R.success("Successfully submitted the order");
    }
}
