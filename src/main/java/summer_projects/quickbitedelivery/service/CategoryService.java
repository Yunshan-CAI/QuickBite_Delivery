package summer_projects.quickbitedelivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.entity.Category;


@Service
public interface CategoryService extends IService<Category> {
    void remove(Long ids);
}

