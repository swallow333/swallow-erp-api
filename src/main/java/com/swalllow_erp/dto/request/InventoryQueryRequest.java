package com.swalllow_erp.dto.request;

import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:11
 */


@Data
public class InventoryQueryRequest {
    private String keyword;          // 商品SKU或名称
    private Integer categoryId;      // 分类筛选
    private Integer minStock;        // 最小库存
    private Integer maxStock;        // 最大库存
    private Boolean onlyLowStock;    // 只显示低库存
    private Integer pageNum = 1;
    private Integer pageSize = 20;
}