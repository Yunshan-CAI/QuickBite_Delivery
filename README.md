# ErrorCollection 异常集锦

在复刻黑马的“瑞吉外卖”的springboot项目过程中，尤其是在项目前期遇到了一些把我卡住了一两天的error。问朋友、查技术文档最终解决了这些问题，解决异常的过程都实实在在地提高了我解决问题的能力。And that makes programming fun.

但是我自己没有发现这个项目有探讨问题的社区，虽然弹幕上有时候有一些有帮助的solution，但是总归不够系统，且弹幕信息庞杂，有用的信息密度低，因此觉得有必要把我在创建项目过程中碰到的觉得会比较普遍的问题总结一下，希望会帮到一些人:)

# Errors (按项目进度排序)


## day02 

在根据视频里的代码对WebMvcConfig这个类进行编辑，视频里与给的资料里的这个类的addResourceHandlers方法都如下：


```
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }
```

程序可以跑，但是在浏览器里面加载页面就会出现 "WhileLabel Error Page"：


![error schreenshot](https://github.com/Yunshan-CAI/QuickBite_Delivery/blob/513300868a726fa1a6e5548066d14626dff7c6bc/errors/screenshot1)


调试了相当一段时间，最后根据弹幕提示找到了csdn里的[这一个帖子](https://blog.csdn.net/qq_69626670/article/details/127584663)，
问题在于WebMvcConfig这个类继承WebMvcConfigSupport之后，会导致默认配置被覆盖，需要重新配置静态资源。

方法重写如下，注意根据你自己的目录结构进行修改，我的目录结构基本跟黑马的一致：


```
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/");

        super.addResourceHandlers(registry);
    }
```


## day04 

### BeanCreationException & IllegalArgumentException

在实现上传功能时，在yml里面path的书写格式要注意，应该是这样的：

```
项目名:
  path: 存放的文件夹路径
```
注意项目名一定要顶格写，path冒号后面要留空格，文件夹路径后面要加/，如 /Users/wentibaobao/Desktop/QuickBite Delivery/upload/

否则会容易出现BeanCreationException和IllegalArgumentException。

### 分页查询的其他解决方案

1) “官方”解决方案

在62节中为了实现菜品的分类查询中的信息完善（因为category_name在category表而不是dish表中），黑马的DishController里面的page方法如下，代码注释了我自己的理解，原谅我的中英夹杂：

```
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

         //create a pagination object
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        //create a new page object with type dish
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);

         //create a lambdaquerywrapper
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //query the dish info by the dish name 拿到了主体信息
        dishLambdaQueryWrapper.like(name != null, Dish::getName, name);

        //把数据传到了pageInfo里面
        dishService.page(pageInfo, dishLambdaQueryWrapper);

        //copy the values in page<dish> object to page<dishdto> but exclude the List<T> records,which is optional
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        //get the whole package of dish info from pageInfo object 拿到主体信息里面的相关信息
        List<Dish> records = pageInfo.getRecords();

        //把主体信息拆开，通过与categoryservice联动得到缺失的信息，把主体信息和新得到的信息都封装进另一个list里面
        List<DishDto> list = records.stream().map(item -> {
            //create a new dishdto object
            DishDto dishDto = new DishDto();

            //copy the values in every item (dish type) in the list to the new created dishdto
            BeanUtils.copyProperties(item, dishDto);

            //get every item's category id
            Long categoryId = item.getCategoryId();

            //use category service to find the corresponding category object with the id
            Category category = categoryService.getById(categoryId);

            //get category's name
            String categoryName = category.getName();

            //give this value, the categoryName to the dishdto object which has this variable
            dishDto.setCategoryName(categoryName);

            //return dishdto rather than the item in the list, this will make it a list of dishdtos
            return dishDto; 
        }).collect(Collectors.toList());

        //give the list of dishdtos to the dishstopage object 把list再封装进另一个page里面
        dishDtoPage.setRecords(list);

        return R.success(resultPage);

        //总结：这个写法之所以感觉复杂是因为涉及categoryname->category; category id->item/dish->list<dish> records->page; dishsto->list<dishdto> records->page这些层级在
        //还涉及了page<dish>->page<dishdto>, item/dish->dishdto 的拷贝
    }
```

但是可以看到这个方法写得相当复杂且代码较多，全部都堆积在controller类里面，mapper和serviceImpl里面几乎没有什么代码。

问题：是否可以把业务逻辑代码放到service层里面去？现在都堆积在controller里面实在并不美观。

2) 用sql进行联表查询

受弹幕区的建议，我尝试用sql进行联表查询，也遇到了好些问题，最终做了出来，我觉得对于新手来说能真的跑通还是有难度的。

上代码，DishController里的：

```
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        // 创建分页对象
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);

        // 调用service方法进行分页查询
        Page<DishDto> resultPage = dishService.listWithCategory(dishDtoPage, name);

        return R.success(resultPage);
    }
```

DishService:

```
Page<DishDto> listWithCategory(Page<DishDto> page, String name);
```

DishServiceImpl:

```
    @Override
    public Page<DishDto> listWithCategory(Page<DishDto> page, String name) {
        List<DishDto> dishDtos = dishMapper.listWithCategory(page, name);
        page.setRecords(dishDtos);
        return page;
    }
```

DishMapper:

```
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    @Select("SELECT d.*, c.name AS category_name " +
            "FROM dish d " +
            "LEFT JOIN category c ON d.category_id = c.id " +
            "WHERE d.name LIKE CONCAT('%', COALESCE(#{name}, ''), '%')")
    List<DishDto> listWithCategory(Page<DishDto> page, @Param("name") String name);
}
```

这样写controller里的方法会简洁很多。

3) 用 MyBatis 的 XML 配置文件进行数据库映射和查询

整个项目都用的是mybatisplus，但是我也想用mybatis也尝试一下。先在resources包下面建一个DishMapper.xml文件：

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="summer_projects.quickbitedelivery.mapper.DishMapper">
    <select id="selectDtos" resultType="summer_projects.quickbitedelivery.dto.DishDto">
        SELECT d.*, c.name AS category_name
        FROM dish d
        LEFT JOIN category c ON d.category_id = c.id
        WHERE d.name LIKE CONCAT('%', COALESCE(#{name}, ''), '%')
    </select>
</mapper>

```
注意id、namespace和resultType要与你自己的实际情况一致。

DishMapper类：

```
    // 方法定义，使用XML配置SQL
    List<DishDto> selectDtos(Page<DishDto> page, @Param("name") String name);
```

DishService类： 

```
    Page<DishDto> getDtos(int page, int pageSize, String name);
```

DishServiceImpl类：

```
    @Override
    public Page<DishDto> getDtos(int page, int pageSize, String name) {
        Page<DishDto> pageParam = new Page<>(page, pageSize);
        List<DishDto> dishList = dishMapper.selectDtos(pageParam, name);
        pageParam.setRecords(dishList);
        return pageParam;
    }
```
mybatisplus 实现连表查询复杂在网上我看到有很多人诟病，我尝试的两种方法都涉及写sql。

不知道在业界这种问题一般是怎么解决的？或者公司里一般不用mybatisplus？有懂行的朋友欢迎交流，想知道什么是这种情况下比较好的practice。

## day05 

### ClassCastException

套餐的分页查询遇到了几乎跟菜品分页查询一样的问题，解决方案也与上面的类似👆。

这一次我尝试自己写page里面的方法，结果遇到了ClassCastException，我的错误版本如下：

```
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Setmeal::getName, name);
        setmealService.page(pageInfo, wrapper);

        Page<SetmealDto> dtoPage = new Page<>();
        //copy the properties of pageInfo to dtoPage
        BeanUtils.copyProperties(pageInfo, dtoPage);

        //get the records from dtoPage, 这时候的范型是<Setmeal>
        List<SetmealDto> list = dtoPage.getRecords();

        List<SetmealDto> collect = list.stream().map(item -> {
            //创建setmealdto对象
            SetmealDto setmealDto = new SetmealDto();

            //把item里的属性复制过去
            BeanUtils.copyProperties(item, setmealDto);

            //取到category_id
            Long categoryId = item.getCategoryId();

            //取到category对象 根据category_id
            Category category = categoryService.getById(categoryId);

            //给setmealdto里面的category赋值
            setmealDto.setCategoryName(category.getName());

            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }
```
我知道复制到dtoPage里面的范型实际上仍然是Setmeal类型而非SetmealDto类型的，我通过断点观察传过来的数据也验证了这一点。

但是我的想法是，我知道 List<SetmealDto> list = dtoPage.getRecords()里list的范型实际上是Setmeal类型的，but why it hurts?

正确解法这里是这样写的：List<Setmeal> list = pageInfo.getRecords()，得到list的范型也是Setmeal，那么我的解法为什么不行呢？

虽然我的list名义上范型是SetmealDto，但它实际上是Setmeal类型的不就行了吗？

Chat的解释是这样的：

>你尝试将 List<Setmeal> 直接当作 List<SetmealDto> 使用。虽然在运行时，这两者都被擦除成 List，但编译器在编译时会检查 List<Setmeal> 和 List<SetmealDto> 之间的兼容性。由于它们是不同的类型，编译器会防止你直接将 List<Setmeal> 转换为 List<SetmealDto>。

>在编译阶段，这种不匹配会通过编译警告或错误提示出来。即使这种转换在某些情况下被强制允许，实际在运行时依然可能会失败，导致 ClassCastException。

虽然SetmealDto继承了Setmeal，但是，

>在 Java 中，泛型类型在继承关系上是不可变的，即：

>   SetmealDto 是 Setmeal 的子类，但 List<SetmealDto> 不是 List<Setmeal> 的子类。
>   List<Setmeal> 和 List<SetmealDto> 是完全不同的类型，不能相互转换。



# Things to improve：

1) 到底哪些代码可以写到controller层，哪些写在service mapper层，需要明确一下然后把代码清理一下。
2) 试着用myBatis 把项目做一遍，不用mp了，这样的话更底层，并且也可以体验一下两种框架的优缺点。
3) 回去看看移动端的用户状态是怎么搞的，如果不要钱的话就弄上，把代码整理一遍，现在感觉有些乱。


