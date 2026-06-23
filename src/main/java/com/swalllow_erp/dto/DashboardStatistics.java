package com.swalllow_erp.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:36
 */

@Data
public class DashboardStatistics {
    // 顶部核心指标
    private Integer totalProducts;
    private Integer totalOrders;
    private BigDecimal totalSales;
    private BigDecimal totalPurchase;

    // 待处理
    private Integer pendingOrders;
    private Integer pendingPurchaseOrders;
    private Integer lowStockCount;

    // 今日统计
    private Integer todayOrders;
    private BigDecimal todaySales;

    // 趋势数据
    private List<SalesTrend> salesTrend;
    private List<SalesTrend> purchaseTrend;

    // 排行
    private List<ProductRank> topProducts;

    // 近期动态
    private List<OrderActivity> recentOrders;

    @Data
    public static class SalesTrend {
        private String date;
        private BigDecimal amount;
    }

    @Data
    public static class ProductRank {
        private Integer productId;
        private String productName;
        private String productSku;
        private Integer totalQuantity;
        private BigDecimal totalAmount;
    }

    @Data
    public static class OrderActivity {
        private String orderNo;
        private String customerName;
        private BigDecimal amount;
        private String statusName;
        private String createTime;
    }
}