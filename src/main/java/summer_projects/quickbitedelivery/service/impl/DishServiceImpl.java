package summer_projects.quickbitedelivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.dto.DishDto;
import summer_projects.quickbitedelivery.entity.Dish;
import summer_projects.quickbitedelivery.entity.DishFlavor;
import summer_projects.quickbitedelivery.mapper.DishMapper;
import summer_projects.quickbitedelivery.service.DishFlavorService;
import summer_projects.quickbitedelivery.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * add a new dish and guarantee the flavors are saved as well
     *
     * @param dishDto
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //save basic info of dish into the table "dish"
        this.save(dishDto);

        //get the dish id
        Long id = dishDto.getId();

        //add the dish_id to the dish flavor info package
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map(item -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());


        //save the flavor related info to the flavor table
        dishFlavorService.saveBatch(flavors);

    }
}
