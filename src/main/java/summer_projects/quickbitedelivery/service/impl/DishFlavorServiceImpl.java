package summer_projects.quickbitedelivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.entity.DishFlavor;
import summer_projects.quickbitedelivery.mapper.DishFlavorMapper;
import summer_projects.quickbitedelivery.service.DishFlavorService;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
