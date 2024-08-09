package summer_projects.quickbitedelivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.dto.SetmealDto;
import summer_projects.quickbitedelivery.entity.Setmeal;
import summer_projects.quickbitedelivery.mapper.SetmealMapper;
import summer_projects.quickbitedelivery.service.SetmealService;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
