package summer_projects.quickbitedelivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.entity.User;
import summer_projects.quickbitedelivery.mapper.UserMapper;
import summer_projects.quickbitedelivery.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
