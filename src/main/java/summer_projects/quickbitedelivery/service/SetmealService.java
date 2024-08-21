package summer_projects.quickbitedelivery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import summer_projects.quickbitedelivery.dto.SetmealDto;
import summer_projects.quickbitedelivery.entity.Setmeal;
import summer_projects.quickbitedelivery.entity.SetmealDish;

import java.util.List;

@Service
public interface SetmealService extends IService<Setmeal> {
    void saveWithDishes(SetmealDto setmealDto);

    void deleteWithDishes(List<Long> ids);

    Page page(int page, int pageSize, String name);

    void setStatus(Long ids, int statusNum);

    List<SetmealDto> getSetmealDtos(Setmeal setmeal);

}
