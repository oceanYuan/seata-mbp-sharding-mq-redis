package io.seata.product.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: OceanYuan
 * @Date: 2022/3/3 16:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVo implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String productName;

    private Integer stock;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
