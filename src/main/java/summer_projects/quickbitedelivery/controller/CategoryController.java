package summer_projects.quickbitedelivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.entity.Category;
import summer_projects.quickbitedelivery.service.CategoryService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * add a new category
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("Category info {}", category);
        categoryService.save(category);
        return R.success("Successfully added a category");
    }

    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("要删除的类别id: {}", ids);
        categoryService.remove(ids);
        return R.success("Successfully removed the category");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("The updated category info: {}", category);
        categoryService.updateById(category);
        return R.success("The category has been updatedd.");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

}
