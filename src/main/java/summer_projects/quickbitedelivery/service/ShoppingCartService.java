package summer_projects.quickbitedelivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import summer_projects.quickbitedelivery.entity.ShoppingCart;


public interface ShoppingCartService extends IService<ShoppingCart> {

    ShoppingCart add(ShoppingCart shoppingCart);

    void deleteItems();
}
