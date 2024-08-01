package summer_projects.quickbitedelivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.dto.DishDto;
import summer_projects.quickbitedelivery.entity.Dish;

@Service
public interface DishService extends IService<Dish> {
    //add the dishes, operate two tables
    public void saveWithFlavor(DishDto dishDto);
}
