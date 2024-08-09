package summer_projects.quickbitedelivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import summer_projects.quickbitedelivery.dto.SetmealDto;
import summer_projects.quickbitedelivery.entity.Setmeal;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
}
