package com.swalllow_erp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:26
 */

@Data
public class SaleOrder {
    private Integer id;
    private String orderNo;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private LocalDateTime orderDate;
    private Integer totalQuantity;
    private BigDecimal totalAmount;
    private Integer status;
    private String remark;
    private Integer createBy;
    private LocalDateTime createTime;
    private Integer updateBy;
    private LocalDateTime updateTime;

    // 非数据库字段
    @TableField(exist = false)
    private List<SaleOrderDetail> details;
}