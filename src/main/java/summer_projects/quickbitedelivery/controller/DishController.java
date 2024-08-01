package summer_projects.quickbitedelivery.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.dto.DishDto;
import summer_projects.quickbitedelivery.service.DishFlavorService;
import summer_projects.quickbitedelivery.service.DishService;
import summer_projects.quickbitedelivery.service.impl.DishServiceImpl;

@RestController
@RequestMapping("/dish")
@Slf4j
@Transactional
/**
 * add the new dish
 */
public class DishController {


    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("Successfully added a dish");
    }
}
