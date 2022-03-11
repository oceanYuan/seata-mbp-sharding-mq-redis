package io.seata.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.seata.product.entity.Product;

/**
 * @Author: OceanYuan
 * @Date: 2022/3/3 15:57
 */
public interface ProductService extends IService<Product> {


     void minusStock();

     int minusStockByList(Long id);

     int minusStockByHash(Long id,String name);

     int preAc(Long id);

     int preAc();

     Product one(Long id);
}
