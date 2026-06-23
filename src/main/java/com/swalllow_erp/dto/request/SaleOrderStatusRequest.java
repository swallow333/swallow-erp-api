package com.swalllow_erp.dto.request;


import lombok.Data;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:28
 */

@Data
public class SaleOrderStatusRequest {
    private Integer status;
    private String remark;
}