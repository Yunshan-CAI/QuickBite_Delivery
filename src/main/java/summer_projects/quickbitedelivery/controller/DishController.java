package summer_projects.quickbitedelivery.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.dto.DishDto;
import summer_projects.quickbitedelivery.entity.Dish;
import summer_projects.quickbitedelivery.service.CategoryService;
import summer_projects.quickbitedelivery.service.DishFlavorService;
import summer_projects.quickbitedelivery.service.DishService;

import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
@Transactional
/**
 * add the new dish
 */
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping("/status/{statusNum}")
    public R<String> setStatus(@RequestParam List<Long> ids, @PathVariable int statusNum) {
        dishService.setStatus(ids, statusNum);
        return R.success("Successfully changed dish status");
    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("Successfully added a dish");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        //解决方案三：用 MyBatis 的 XML 配置文件进行数据库映射和查询
        Page<DishDto> resultPage = dishService.getDtos(page, pageSize, name);

        return R.success(resultPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable Long id) {
        DishDto dishById = dishService.getDishById(id);
        return R.success(dishById);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("Successfully updated a dish");
    }

    @GetMapping("/list")
    public R<List<DishDto>> getDishDtoByCategory(Dish dish) {
        List<DishDto> dishDtos = dishService.getDishDto(dish);

        return R.success(dishDtos);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        dishService.delete(ids);
        return R.success("Successfully deleted the dishes");
    }
}

