package io.seata.product.controller;

import io.seata.product.entity.Product;
import io.seata.product.service.ProductService;
import io.seata.product.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;


    @PutMapping("/minus/stock")
    public ResponseEntity<Void> minusStock() {
        productService.minusStock();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //redis 减库存
    @PutMapping("/minus/upStock")
    public R minusStockFromRedis(@RequestParam Long id,@RequestParam String name) {
        int i = productService.minusStockByHash(id,name);
        if (i != 1){
            return new R(200,true,"已抢完");
        }
        return new R(200,true,"恭喜抢到");
    }

    //预热
    @PostMapping("/product/putInfo")
    public R putInfo(@RequestParam(required = false) Long id){

        int flag;
        if (Optional.ofNullable(id).isPresent()){
             flag = productService.preAc(id);
        }else {
            flag = productService.preAc();
        }
       //55*8

        return new R(flag,true,null,"预热成功");
    }


    @GetMapping("/getOne")
    public R getGoodByLimit(@RequestParam Long id){
        if (Optional.ofNullable(id).isPresent()){
            Product one = productService.one(id);
            return new R(one);
        }else {
            return new R("查无此商品");
        }

    }
}
