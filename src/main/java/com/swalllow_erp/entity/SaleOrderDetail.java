package com.swalllow_erp.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;



/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:27
 */
@Data
public class SaleOrderDetail {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 非数据库字段
    private String productName;
    private String productSku;
}