package summer_projects.quickbitedelivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import summer_projects.quickbitedelivery.dto.DishDto;
import summer_projects.quickbitedelivery.entity.Dish;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Select("SELECT d.*, c.name AS category_name " +
            "FROM dish d " +
            "LEFT JOIN category c ON d.category_id = c.id " +
            "WHERE d.name LIKE CONCAT('%', COALESCE(#{name}, ''), '%')")
    List<DishDto> listWithCategory(Page<DishDto> page, @Param("name") String name);

    // 方法定义，使用XML配置SQL
    List<DishDto> selectDtos(Page<DishDto> page, @Param("name") String name);

}
