package summer_projects.quickbitedelivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import summer_projects.quickbitedelivery.common.CustomException;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.dto.SetmealDto;
import summer_projects.quickbitedelivery.entity.Category;
import summer_projects.quickbitedelivery.entity.Dish;
import summer_projects.quickbitedelivery.entity.Setmeal;
import summer_projects.quickbitedelivery.entity.SetmealDish;
import summer_projects.quickbitedelivery.service.CategoryService;
import summer_projects.quickbitedelivery.service.SetmealDishService;
import summer_projects.quickbitedelivery.service.SetmealService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDishes(setmealDto);
        return R.success("Successfully saved a setmeal");
    }

    /**
     * 对套餐进行分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page resultPage = setmealService.page(page, pageSize, name);
        return R.success(resultPage);
    }

    /**
     * 把套餐设为停售/起售状态
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/{statusNum}")
    public R<String> setStatus(Long ids, @PathVariable int statusNum) {
        setmealService.setStatus(ids, statusNum);
        return R.success("Successfully changed setmeal status");
    }

    /**
     * 删除在停售状态的套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteWithDishes(ids);
        return R.success("Successfully deleted the setmeal");
    }

    //数据传到前端了但不知道为什么套餐包含的菜品无法显示在页面上，我觉得是因为前端没有写
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId+'_'+#setmeal.status")
    public R<List<SetmealDto>> getSetmealDtos(Setmeal setmeal) {
        List<SetmealDto> setmealDtos = setmealService.getSetmealDtos(setmeal);
        return R.success(setmealDtos);
    }
}


