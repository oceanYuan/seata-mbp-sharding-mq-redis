package io.seata.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.seata.ControllerAdvice;
import io.seata.order.entity.Order;
import io.seata.order.service.OrderService;
import io.seata.order.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class OrderController extends ControllerAdvice {

    @Autowired
    private OrderService orderService;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;


    @PostMapping("/seata/test")
    public ResponseEntity<Void> seataDemo(Boolean hasError) {
        orderService.seataDemo(hasError);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/seata/restock")
    public R reduce(@RequestParam @Validated Integer x){
        Integer reduce = orderService.reduce(x);
        if (reduce!=1){
            return new R(400,false,null,"请求失败");
        }
        return new R(200,true,reduce,"请求成功");
    }


    @GetMapping("/seata/list")
    public R listForP(){
        List<Order> list = (List<Order>) redisTemplate.opsForList().leftPop("listWithNoLimit");
        if (!Optional.ofNullable(list).isPresent()){
            List<Order> orders = orderService.list(null);
            redisTemplate.opsForList().leftPush("listWithNoLimit",orders);
            return new R(orders);
        }

        return new R(list);

    }


    @GetMapping("/seata/{id}")
    public R li(@PathVariable("id") Long id){
        Order order = (Order) redisTemplate.opsForHash().get(id.toString(), id.toString());
        Optional<Order> optional = Optional.ofNullable(order);
        if (!optional.isPresent()) {
            order = orderService.getById(id);
            if(!Optional.ofNullable(order).isPresent()){
                return new R(400,false,null,"查无此条消息");
            }
            redisTemplate.opsForHash().put(id.toString(),id.toString(),order);
        }

        return new R(200,true,order,"查询成功");

    }

    @GetMapping("/seata/buy/{buyNum}")
    public R li2(@PathVariable("buyNum") @Validated Integer buyNum){
        QueryWrapper<Order> qw = new QueryWrapper<>();

        LambdaQueryWrapper<Order> eq = qw.lambda().eq(Order::getBuyNum, buyNum);
        List<Order> orders = orderService.list(eq);


        return new R(200,true,orders,"查询成功");

    }
}
