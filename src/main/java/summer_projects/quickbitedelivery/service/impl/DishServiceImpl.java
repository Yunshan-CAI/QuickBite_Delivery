package summer_projects.quickbitedelivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.dto.DishDto;
import summer_projects.quickbitedelivery.entity.Dish;
import summer_projects.quickbitedelivery.entity.DishFlavor;
import summer_projects.quickbitedelivery.mapper.DishMapper;
import summer_projects.quickbitedelivery.service.DishFlavorService;
import summer_projects.quickbitedelivery.service.DishService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishMapper dishMapper;

    /**
     * add a new dish and guarantee the flavors are saved as well
     *
     * @param dishDto
     */
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
        dishFlavorService.saveBatch(dishDto.getFlavors());

    }

    @Override
    public Page<DishDto> listWithCategory(Page<DishDto> page, String name) {
        List<DishDto> dishDtos = dishMapper.listWithCategory(page, name);
        page.setRecords(dishDtos);
        return page;
    }

    @Override
    public Page<DishDto> getDtos(int page, int pageSize, String name) {
        Page<DishDto> pageParam = new Page<>(page, pageSize);
        List<DishDto> dishList = dishMapper.selectDtos(pageParam, name);
        pageParam.setRecords(dishList);
        return pageParam;
    }

    @Override
    public DishDto getDishById(Long id) {
        Dish dish = dishMapper.selectById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper();
        wrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(wrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //save basic info of dish into the table "dish"
        dishMapper.updateById(dishDto);

        //delete the original dish flavor
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(wrapper);

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

    @Override
    public List<DishDto> getDishDto(Dish dish) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //查询状态为1（在售状态）的菜品
        wrapper.eq(Dish::getStatus, 1);
        List<Dish> list = this.list(wrapper);

        List<DishDto> dtoList = new ArrayList<>();
        BeanUtils.copyProperties(list, dtoList);

        List<DishDto> collect = list.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());
        return collect;
    }

}
