package summer_projects.quickbitedelivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import summer_projects.quickbitedelivery.entity.AddressBook;
import summer_projects.quickbitedelivery.entity.Category;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
