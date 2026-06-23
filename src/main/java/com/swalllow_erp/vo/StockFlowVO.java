package com.swalllow_erp.vo;



import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:10
 */

@Data
public class StockFlowVO {
    private Integer id;
    private Integer productId;
    private String productSku;
    private String productTitle;
    private Integer flowType;          // 1入库 2出库 3盘点
    private String flowTypeName;       // 入库/出库/盘点
    private Integer changeQuantity;    // 变动数量（正/负）
    private Integer beforeQuantity;    // 变动前库存
    private Integer afterQuantity;     // 变动后库存
    private String sourceType;         // PURCHASE_IN / SALE_OUT
    private String sourceId;           // 来源单号
    private String remark;
    private LocalDateTime createTime;
}