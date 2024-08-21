package summer_projects.quickbitedelivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.common.BaseContext;
import summer_projects.quickbitedelivery.entity.ShoppingCart;
import summer_projects.quickbitedelivery.mapper.ShoppingCartMapper;
import summer_projects.quickbitedelivery.service.ShoppingCartService;

import java.time.LocalDateTime;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        //因为没有登陆所以手动设置user_id
        shoppingCart.setUserId(1L);

        //看该条是否已经在数据库中存在,存在数量加一，不存在添加
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        //看是单独的菜品还是套餐，查询条件不同
        if (shoppingCart.getDishId() != null) {
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //看是否已经存入
        int count = this.count(wrapper);
        ShoppingCart cartExisted = this.getOne(wrapper);
        //有的话数量加一
        if (count > 0) {
            int num = cartExisted.getNumber() + 1;
            cartExisted.setNumber(num);
            this.update(cartExisted, wrapper);
            return cartExisted;
        } else {
            //没有的话存入，并且设置create_time
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            this.save(shoppingCart);
            return shoppingCart;
        }
    }

    @Override
    public void deleteItems() {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        //根据我自己设置的user_id进行删除
        wrapper.eq(ShoppingCart::getUserId, 1234543212L);
        this.remove(wrapper);
    }
}
