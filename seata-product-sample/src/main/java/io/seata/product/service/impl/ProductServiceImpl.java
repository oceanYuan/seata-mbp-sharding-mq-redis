package io.seata.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.product.entity.Product;
import io.seata.product.mapper.ProductMapper;
import io.seata.product.service.ProductService;
import io.seata.product.vo.ProductVo;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private Redisson redisson;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void minusStock() {
        productMapper.minusStock();
    }

    @Override
    public int minusStockByHash(Long id,String name) {

        if (!Optional.ofNullable(id).isPresent()) {
            return 0;
        }
        String key = "lock_hash";

        RLock redissonLock = redisson.getLock(key);

//        new Thread(() -> {
            redissonLock.lock();
            try {


                Integer stock = (Integer) redisTemplate.opsForHash().get(id.toString(), name);
                while (stock == 0){
                    productMapper.minusStockById(id);
                    return 0;
                }

                redisTemplate.opsForHash().increment(id.toString(),name,-1);


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redissonLock.unlock();
            }
//        }).start();

        return 1;
    }

    @Override
    public int minusStockByList(Long id) {
        if (!Optional.ofNullable(id).isPresent()) {
            return 0;
        }
        String key = "lock";
        RLock redissonLock = redisson.getLock(key);

        new Thread(() -> {
            redissonLock.lock();
            try {
                List<Product> preAc = (List<Product>) redisTemplate.opsForList().leftPop("preAc");
                if (!Optional.ofNullable(preAc).isPresent()) {
                    for (int i = 0; i < preAc.size(); i++) {
                        Product product = preAc.get(i);
                        if (product.getId().equals(id)) {
                            Integer stock = product.getStock();
                            if (stock == 0) {
                                productMapper.minusStockById(id);
                                return;
                            }
                            stock = stock - 1;
                            product.setStock(stock);
                            //todo
                            break;
                        }

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redissonLock.unlock();
            }
        }).start();

        return 1;
    }

    @Override
    public int preAc(Long id) {

        QueryWrapper<Product> qw = new QueryWrapper<>();
        qw.lambda().eq(Product::getFlag, 1).eq(Product::getId,id);
        Product product = productMapper.selectOne(qw);

        if (Optional.ofNullable(product).isPresent()){
            Integer stock = product.getStock();
            String productName = product.getProductName();
            redisTemplate.opsForHash().put(id.toString(), productName, stock);
            this.redisTemplate.expire(id.toString(),60,TimeUnit.MINUTES);
            return 1;
        }
//        products.stream().forEach(item -> {
//            String idstr = item.getId().toString();
//            Date createTime = item.getCreateTime();
//            Date updateTime = item.getUpdateTime();
//            Integer stock = item.getStock();
//            String productName = item.getProductName();
//
//            ProductVo productVo = ProductVo.builder()
//                    .id(idstr).productName(productName)
//                    .createTime(createTime).stock(stock)
//                    .updateTime(updateTime).build();
//
//            redisTemplate.opsForHash().put(idstr, productName, productVo);
//            this.redisTemplate.expire(idstr,60,TimeUnit.MINUTES);
//
//        });
        return 0;
    }

    @Override
    public int preAc() {

        QueryWrapper<Product> qw = new QueryWrapper<>();
        qw.lambda().eq(Product::getFlag, 1);
        List<Product> products = productMapper.selectList(qw);

        redisTemplate.opsForList().leftPush("preAc", products);


        return 1;
    }

    @Override
    public Product one(Long id) {

        QueryWrapper<Product> qw  = new QueryWrapper<>();
        qw.lambda().eq(Product::getId,id);
        Product product = productMapper.selectOne(qw);
        return product;
    }
}
