package io.seata.order.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: OceanYuan
 * @Date: 2022/2/28 11:42
 */
@Data
@NoArgsConstructor
public class R<T> implements Serializable {


    private static final long serialVersionUid = 1L;

    private int code;

    private boolean success;

    private T data;

    private String msg;


    public R(T data) {
        this.data = data;
    }

    public R(int code, boolean success, T data, String msg) {
        this.code = code;
        this.success = success;
        this.data = data;
        this.msg = msg;
    }
}
