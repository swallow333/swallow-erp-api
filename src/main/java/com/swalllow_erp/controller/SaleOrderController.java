package com.swalllow_erp.controller;


import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.dto.request.SaleOrderCreateRequest;
import com.swalllow_erp.dto.request.SaleOrderQueryRequest;
import com.swalllow_erp.dto.request.SaleOrderStatusRequest;
import com.swalllow_erp.entity.SaleOrder;
import com.swalllow_erp.service.SaleOrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:30
 */

@RestController
@RequestMapping("/sale-orders")
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;

    @PostMapping
    public CommonResult<SaleOrder> create(@RequestBody @Valid SaleOrderCreateRequest request) {
        Integer userId = 1;
        SaleOrder order = saleOrderService.createOrder(request, userId);
        return CommonResult.success(order);
    }

    @PostMapping("/page")
    public CommonResult<PageInfo<SaleOrder>> queryPage(@RequestBody SaleOrderQueryRequest request) {
        PageInfo<SaleOrder> page = saleOrderService.queryPage(request);
        return CommonResult.success(page);
    }

    @GetMapping("/{id}")
    public CommonResult<SaleOrder> getDetail(@PathVariable Integer id) {
        SaleOrder order = saleOrderService.getOrderWithDetails(id);
        if (order == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        return CommonResult.success(order);
    }

    @PutMapping("/{id}/status")
    public CommonResult<Void> updateStatus(@PathVariable Integer id,
                                           @RequestBody SaleOrderStatusRequest request) {
        Integer userId = 1;
        saleOrderService.updateStatus(id, request, userId);
        return CommonResult.success("操作成功", null);
    }

    @PutMapping("/{id}/cancel")
    public CommonResult<Void> cancel(@PathVariable Integer id) {
        Integer userId = 1;
        saleOrderService.cancelOrder(id, userId);
        return CommonResult.success("取消成功", null);
    }
}