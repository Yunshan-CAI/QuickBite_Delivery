package summer_projects.quickbitedelivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.common.CustomException;
import summer_projects.quickbitedelivery.dto.SetmealDto;
import summer_projects.quickbitedelivery.entity.Category;
import summer_projects.quickbitedelivery.entity.Setmeal;
import summer_projects.quickbitedelivery.entity.SetmealDish;
import summer_projects.quickbitedelivery.mapper.SetmealMapper;
import summer_projects.quickbitedelivery.service.CategoryService;
import summer_projects.quickbitedelivery.service.SetmealDishService;
import summer_projects.quickbitedelivery.service.SetmealService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void saveWithDishes(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long id = setmealDto.getId();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void deleteWithDishes(List<Long> ids) {
        for (Long id : ids) {
            if (this.getById(id).getStatus() == 0) {
                this.removeById(id);
                LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(id != null, SetmealDish::getSetmealId, id);
                setmealDishService.remove(wrapper);
            } else {
                throw new CustomException("套餐正在售卖中，不能删除");
            }
        }
    }

    @Override
    public Page page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Setmeal::getName, name);
        this.page(pageInfo, wrapper);

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
        return dtoPage;
    }

    @Override
    public void setStatus(Long ids, int statusNum) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ids != null, Setmeal::getId, ids);

        Setmeal setmeal = this.getById(ids);
        setmeal.setStatus(statusNum);

        this.update(setmeal, wrapper);
    }

    @Override
    public List<SetmealDto> getSetmealDtos(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        //查询状态为1（在售状态）的套餐
        wrapper.eq(Setmeal::getStatus, 1);
        List<Setmeal> list = this.list(wrapper);

        List<SetmealDto> collect = list.stream().map(item -> {
            Long id = item.getId();
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, id);
            List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);

            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setSetmealDishes(setmealDishes);
            return setmealDto;
        }).collect(Collectors.toList());

        return collect;
    }

}
