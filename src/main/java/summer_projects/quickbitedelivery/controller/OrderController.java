package summer_projects.quickbitedelivery.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
