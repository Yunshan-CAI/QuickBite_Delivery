package summer_projects.quickbitedelivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.dto.DishDto;
import summer_projects.quickbitedelivery.entity.Category;
import summer_projects.quickbitedelivery.entity.Dish;
import summer_projects.quickbitedelivery.service.CategoryService;
import summer_projects.quickbitedelivery.service.DishFlavorService;
import summer_projects.quickbitedelivery.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

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
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("Successfully added a dish");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //create a pagination object
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        //create a lambdaquerywrapper
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //query the dish info by the dish name 拿到了主体信息
        dishLambdaQueryWrapper.like(name != null, Dish::getName, name);
        //return the dish info? 再研究
        dishService.page(pageInfo, dishLambdaQueryWrapper);

        //create a new page object with type dish dto Page研究一下
        Page<DishDto> dishDtoPage = new Page<>();

        //copy the values in page<dish> object to page<dishdto> but exclude the List<T> records,which is optional
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        //get the whole package of dish info from pageInfo object 拿到主体信息里面的相关信息
        List<Dish> records = pageInfo.getRecords();

        //把主体信息拆开，通过与categoryservice联动得到缺失的信息，把主体信息和新得到的信息都封装进另一个list里面
        List<DishDto> list = records.stream().map(item -> {
            //create a new dishdto object
            DishDto dishDto = new DishDto();
            //copy the values in every item (dish type) in the list to the new created dishdto
            BeanUtils.copyProperties(item, dishDto);
            //get every item's category id
            Long categoryId = item.getCategoryId();
            //use category service to find the corresponding category object with the id
            Category category = categoryService.getById(categoryId);
            //get category's name
            String categoryName = category.getName();
            //give this value, the categoryName to the dishdto object which has this variable
            dishDto.setCategoryName(categoryName);
            //return dishdto rather than the item in the list, this will make it a list of dishdtos
            return dishDto; //return这个是什么写法？
        }).collect(Collectors.toList());

        //give the list of dishdtos to the dishstopage object 把list再封装进另一个page里面
        dishDtoPage.setRecords(list);

        //总结：这个写法之所以感觉复杂是因为涉及categoryname->category; category id->item/dish->list<dish> records->page; dishsto->list<dishdto> records->page这些层级在
        //还涉及了page<dish>->page<dishdto>, item/dish->dishdto 的拷贝
        //整体确实有些太麻烦 看看有没有其他的方法

        return R.success(dishDtoPage);
    }
}
