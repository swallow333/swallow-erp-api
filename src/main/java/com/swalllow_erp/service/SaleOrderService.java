package com.swalllow_erp.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.swalllow_erp.entity.SaleOrder;
import com.swalllow_erp.dto.request.SaleOrderCreateRequest;
import com.swalllow_erp.dto.request.SaleOrderQueryRequest;
import com.swalllow_erp.dto.request.SaleOrderStatusRequest;
import com.github.pagehelper.PageInfo;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:29
 */

public interface SaleOrderService extends IService<SaleOrder> {

    SaleOrder createOrder(SaleOrderCreateRequest request, Integer userId);

    PageInfo<SaleOrder> queryPage(SaleOrderQueryRequest request);

    SaleOrder getOrderWithDetails(Integer orderId);

    void updateStatus(Integer orderId, SaleOrderStatusRequest request, Integer userId);

    void cancelOrder(Integer orderId, Integer userId);

    String generateOrderNo();
}