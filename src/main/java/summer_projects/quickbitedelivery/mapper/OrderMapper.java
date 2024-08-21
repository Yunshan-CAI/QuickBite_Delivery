package summer_projects.quickbitedelivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import summer_projects.quickbitedelivery.entity.Orders;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
