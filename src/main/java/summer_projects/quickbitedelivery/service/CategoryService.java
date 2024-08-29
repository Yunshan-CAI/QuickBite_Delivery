package summer_projects.quickbitedelivery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.entity.Category;

import java.util.List;


@Service
public interface CategoryService extends IService<Category> {
    void remove(Long ids);
    Page<Category> page(int page, int pageSize);
    List<Category> list(Category category);
}

