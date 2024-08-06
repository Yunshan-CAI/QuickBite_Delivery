package summer_projects.quickbitedelivery.dto;


import lombok.Data;
import summer_projects.quickbitedelivery.entity.Setmeal;
import summer_projects.quickbitedelivery.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
