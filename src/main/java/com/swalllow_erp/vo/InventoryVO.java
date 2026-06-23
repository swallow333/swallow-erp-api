package com.swalllow_erp.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:10
 */

@Data
public class InventoryVO {
    private Integer productId;
    private String productSku;
    private String productTitle;
    private String productImage;
    private Integer quantity;           // 当前库存
    private Integer lockedQuantity;     // 锁定数量（待出库）
    private Integer availableQuantity;  // 可用数量
    private Integer safetyStock;        // 安全库存阈值（可在商品表加字段）
    private LocalDateTime lastUpdateTime;
}