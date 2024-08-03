package summer_projects.quickbitedelivery.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import summer_projects.quickbitedelivery.entity.Dish;
import summer_projects.quickbitedelivery.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
