package summer_projects.quickbitedelivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import summer_projects.quickbitedelivery.common.CustomException;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.dto.SetmealDto;
import summer_projects.quickbitedelivery.entity.Category;
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
     * save the setmeal info to the database
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long id = setmealDto.getId();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
        }
        setmealDishService.saveBatch(setmealDishes);
        return R.success("Successfully saved a setmeal");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Setmeal::getName, name);
        setmealService.page(pageInfo, wrapper);

        Page<SetmealDto> dtoPage = new Page<>();
        //copy the properties of pageInfo to dtoPage
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<SetmealDto> list = pageInfo.getRecords().stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
     * 把套餐设为停售/起售状态
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/{statusNum}")
    public R<String> setStatus(Long ids, @PathVariable int statusNum) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ids != null, Setmeal::getId, ids);

        Setmeal setmeal = setmealService.getById(ids);
        setmeal.setStatus(statusNum);

        setmealService.update(setmeal, wrapper);
        return R.success("Successfully changed setmeal status");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {

        for (Long id : ids) {
            if (setmealService.getById(id).getStatus() == 0) {
                setmealService.removeById(id);
                LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(id != null, SetmealDish::getSetmealId, id);
                setmealDishService.remove(wrapper);
            } else {
                throw new CustomException("套餐正在售卖中，不能删除");
            }
        }
        return R.success("Successfully deleted the setmeal");
    }
}


