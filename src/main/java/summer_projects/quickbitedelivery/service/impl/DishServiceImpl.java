package summer_projects.quickbitedelivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import summer_projects.quickbitedelivery.common.CustomException;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.dto.DishDto;
import summer_projects.quickbitedelivery.entity.Dish;
import summer_projects.quickbitedelivery.entity.DishFlavor;
import summer_projects.quickbitedelivery.entity.Setmeal;
import summer_projects.quickbitedelivery.entity.SetmealDish;
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
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * add a new dish and guarantee the flavors are saved as well
     *
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto) {
        //添加create_user的信息，因为网页没有传过来。
        // 但是我有让自动生成也不管用，后面再看看怎么优化吧
        //现在先手动设置一下
        dishDto.setCreateUser(1L);
        dishDto.setUpdateUser(1L);

        //save basic info of dish into the table "dish"
        this.save(dishDto);

        //get the dish id
        Long id = dishDto.getId();

        //add the dish_id to the dish flavor info package
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map(item -> {
            item.setDishId(id);
            //dish flavor这个表里也需要手动设置create_user和update_user
            item.setCreateUser(1L);
            item.setUpdateUser(1L);
            return item;
        }).collect(Collectors.toList());

        //save the flavor related info to the flavor table
        dishFlavorService.saveBatch(dishDto.getFlavors());

        //如果某一个类添加了新的菜品，要清理缓存
        String key = "category_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        redisTemplate.delete(key);

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

        //要启动redis以保证这个方法工作
        // 如果某一个类中的任何信息进行了更新，要清理缓存
        String key = "category_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        redisTemplate.delete(key);

    }

    @Override
    public List<DishDto> getDishDto(Dish dish) {

        //动态构造给redis的key
        String key = "category_" + dish.getCategoryId() + "_" + dish.getStatus();
        //先从redis里面获取缓存数据
        List<DishDto> dtoListRedis = (List<DishDto>) redisTemplate.opsForValue().get(key);
        //如果有数据，直接返回
        if (dtoListRedis != null) {
            return dtoListRedis;
        }

        //如果没有，再从数据库里面查询数据
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

        //查询完数据库之后把数据缓存在redis里面
        redisTemplate.opsForValue().set(key, collect);
        return collect;
    }

    @Override
    public void setStatus(List<Long> ids, int statusNum) {
        for (Long id : ids) {
            LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(id != null, Dish::getId, id);

            Dish dish = this.getById(id);
            dish.setStatus(statusNum);

            this.update(dish, wrapper);
        }
    }

    @Override
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            if (this.getById(id).getStatus() == 0) {
                this.removeById(id);
            } else {
                throw new CustomException("菜品正在售卖中，不能删除");
            }
        }
    }

}
