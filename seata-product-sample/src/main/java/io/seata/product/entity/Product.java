package io.seata.product.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("product_info")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String productName;

    private Integer stock;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    private Integer flag;

    private Integer version;
}
