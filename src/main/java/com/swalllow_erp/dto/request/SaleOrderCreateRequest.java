package com.swalllow_erp.dto.request;


import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:27
 */

@Data
public class SaleOrderCreateRequest {

    @NotEmpty(message = "客户名称不能为空")
    private String customerName;

    private String customerPhone;
    private String customerAddress;
    private String remark;

    @NotEmpty(message = "订单明细不能为空")
    @Valid
    private List<OrderDetail> details;

    @Data
    public static class OrderDetail {
        @NotNull(message = "商品不能为空")
        private Integer productId;

        @NotNull(message = "数量不能为空")
        private Integer quantity;

        @NotNull(message = "单价不能为空")
        private BigDecimal price;
    }
}