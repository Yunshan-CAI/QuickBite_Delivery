package summer_projects.quickbitedelivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.entity.AddressBook;
import summer_projects.quickbitedelivery.entity.Category;
import summer_projects.quickbitedelivery.mapper.AddressBookMapper;
import summer_projects.quickbitedelivery.mapper.CategoryMapper;
import summer_projects.quickbitedelivery.service.AddressBookService;
import summer_projects.quickbitedelivery.service.CategoryService;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
