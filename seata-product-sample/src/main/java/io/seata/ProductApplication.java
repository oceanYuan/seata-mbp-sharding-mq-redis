package io.seata;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("io.seata.product.mapper")
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

    @Bean
    public Redisson redisson(){
        Config config = new Config();
        config.useSingleServer().setConnectionMinimumIdleSize(10);
        //主从(单机)
        config.useSingleServer().setAddress("redis://110.40.132.196:6379").setDatabase(0);
        //哨兵
//    config.useSentinelServers().setMasterName("mymaster");
//    config.useSentinelServers().addSentinelAddress("redis://192.168.1.1:26379");
//    config.useSentinelServers().addSentinelAddress("redis://192.168.1.2:26379");
//    config.useSentinelServers().addSentinelAddress("redis://192.168.1..3:26379");
//    config.useSentinelServers().setDatabase(0);
//    //集群
//    config.useClusterServers()
//            .addNodeAddress("redis://192.168.0.1:8001")
//            .addNodeAddress("redis://192.168.0.2:8002")
//            .addNodeAddress("redis://192.168.0.3:8003")
//            .addNodeAddress("redis://192.168.0.4:8004")
//            .addNodeAddress("redis://192.168.0.5:8005")
//            .addNodeAddress("redis://192.168.0.6:8006");
//    config.useSentinelServers().setPassword("xx");//密码设置
        return (Redisson) Redisson.create(config);
    }



}
