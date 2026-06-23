package com.swalllow_erp.dto.request;


import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:28
 */

@Data
public class SaleOrderQueryRequest {
    private String orderNo;
    private String customerName;
    private Integer status;
    private String startDate;
    private String endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}