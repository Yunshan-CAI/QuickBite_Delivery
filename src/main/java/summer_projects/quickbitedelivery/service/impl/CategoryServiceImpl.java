package summer_projects.quickbitedelivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.common.CustomException;
import summer_projects.quickbitedelivery.dto.SetmealDto;
import summer_projects.quickbitedelivery.entity.Category;
import summer_projects.quickbitedelivery.entity.Dish;
import summer_projects.quickbitedelivery.entity.Setmeal;
import summer_projects.quickbitedelivery.mapper.CategoryMapper;
import summer_projects.quickbitedelivery.service.CategoryService;
import summer_projects.quickbitedelivery.service.DishService;
import summer_projects.quickbitedelivery.service.SetmealService;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void remove(Long ids) {
        LambdaQueryWrapper<Dish> dishServiceLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //query the dish table by id to see if the id is connected to certain dish
        dishServiceLambdaQueryWrapper.eq(Dish::getCategoryId, ids);
        int count1 = dishService.count(dishServiceLambdaQueryWrapper);

        //if id related to a dish, can't be removed
        if (count1 > 0) {
            throw new CustomException("A category attached to dish can't be removed");
        }

        //query the setmeal table by id to see if the id is connected to certain setmeal
        LambdaQueryWrapper<Setmeal> setmealServiceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealServiceLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        int count2 = setmealService.count(setmealServiceLambdaQueryWrapper);

        //if id related to a setmeal, can't be removed
        if (count2 > 0) {
            //throws an exception
            throw new CustomException("A category attached to setmeal can't be removed");
        }

        //remove the category
        super.removeById(ids);

    }

    @Override
    public Page<Category> page(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return pageInfo;
    }

    @Override
    public List<Category> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return list;
    }
}
