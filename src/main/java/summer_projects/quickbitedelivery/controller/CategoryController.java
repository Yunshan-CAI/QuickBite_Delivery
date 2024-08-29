package summer_projects.quickbitedelivery.controller;

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
        //手动设置create_user和update_user
        category.setCreateUser(1L);
        category.setUpdateUser(1L);
        categoryService.save(category);
        return R.success("Successfully added a category");
    }

    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize) {
        Page<Category> returnPage = categoryService.page(page, pageSize);
        return R.success(returnPage);
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
        //手动设置update_user
        category.setUpdateUser(1L);
        categoryService.updateById(category);
        return R.success("The category has been updatedd.");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        List<Category> returnList = categoryService.list(category);
        return R.success(returnList);
    }
}
