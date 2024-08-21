package summer_projects.quickbitedelivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import summer_projects.quickbitedelivery.entity.OrderDetail;
import summer_projects.quickbitedelivery.mapper.OrderDetailMapper;
import summer_projects.quickbitedelivery.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
