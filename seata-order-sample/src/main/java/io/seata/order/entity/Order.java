package io.seata.order.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.sun.istack.internal.NotNull;
import groovy.transform.Field;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Insert;

@Data
@TableName("order_info")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

//    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String orderName;

    private Long productId;

    private Integer buyNum;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
