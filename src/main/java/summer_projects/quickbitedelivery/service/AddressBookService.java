package summer_projects.quickbitedelivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import summer_projects.quickbitedelivery.common.R;
import summer_projects.quickbitedelivery.entity.AddressBook;
import summer_projects.quickbitedelivery.entity.Category;

import java.util.List;


public interface AddressBookService extends IService<AddressBook> {
    AddressBook setDefault(AddressBook addressBook);
    AddressBook getDefault();
    List<AddressBook> list(AddressBook addressBook);
}
