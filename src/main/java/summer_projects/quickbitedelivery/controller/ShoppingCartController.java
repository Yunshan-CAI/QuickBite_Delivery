package summer_projects.quickbitedelivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import summer_projects.quickbitedelivery.common.BaseContext;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.entity.ShoppingCart;
import summer_projects.quickbitedelivery.service.ShoppingCartService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 查看购物车
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车...");

        //因为我没有用户id,所以就让返回shopping cart表里所有的数据了
/*        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);*/
        List<ShoppingCart> list = shoppingCartService.list();

        return R.success(list);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        ShoppingCart resultShoppingCart = shoppingCartService.add(shoppingCart);
        return R.success(resultShoppingCart);
    }

    @DeleteMapping("/clean")
    public R<String> delete() {
        shoppingCartService.deleteItems();
        return R.success("Successfully deleted all the items in the shopping cart");
    }
}


