package summer_projects.quickbitedelivery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.dto.DishDto;
import summer_projects.quickbitedelivery.entity.Dish;

import java.util.List;

@Service
public interface DishService extends IService<Dish> {
    //add the dishes, operate two tables
    public void saveWithFlavor(DishDto dishDto);

    //返回一个分页构造器;
    Page<DishDto> listWithCategory(Page<DishDto> page, String name);

    Page<DishDto> getDtos(int page, int pageSize, String name);

    DishDto getDishById(Long id);

    void updateWithFlavor(DishDto dishDto);
}
