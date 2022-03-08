package io.seata.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.seata.order.entity.Order;

/**
 * @Author: OceanYuan
 * @Date: 2022/2/28 14:58
 */

public interface OrderService extends IService<Order> {

    void seataDemo(Boolean hasError);

    Integer reduce(Integer x);
}
