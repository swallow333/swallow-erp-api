package com.swalllow_erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swalllow_erp.entity.Inventory;
import com.swalllow_erp.entity.SaleOrder;
import com.swalllow_erp.entity.SaleOrderDetail;
import com.swalllow_erp.mapper.InventoryMapper;
import com.swalllow_erp.mapper.SaleOrderDetailMapper;
import com.swalllow_erp.mapper.SaleOrderMapper;
import com.swalllow_erp.service.InventoryService;
import com.swalllow_erp.service.SaleOrderService;
import com.swalllow_erp.dto.request.SaleOrderCreateRequest;
import com.swalllow_erp.dto.request.SaleOrderQueryRequest;
import com.swalllow_erp.dto.request.SaleOrderStatusRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:29
 */

@Service
public class SaleOrderServiceImpl extends ServiceImpl<SaleOrderMapper, SaleOrder>
        implements SaleOrderService {

    @Autowired
    private SaleOrderDetailMapper detailMapper;

    @Autowired
    private InventoryService inventoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaleOrder createOrder(SaleOrderCreateRequest request, Integer userId) {
        // 1. 校验库存
        for (SaleOrderCreateRequest.OrderDetail dto : request.getDetails()) {
            Inventory inventory = inventoryService.getByProductId(dto.getProductId());
            if (inventory == null || inventory.getAvailableQuantity() < dto.getQuantity()) {
                throw new RuntimeException("商品库存不足，请调整数量");
            }
        }

        // 2. 生成订单编号
        String orderNo = generateOrderNo();

        // 3. 计算汇总
        int totalQuantity = request.getDetails().stream()
                .mapToInt(SaleOrderCreateRequest.OrderDetail::getQuantity)
                .sum();
        BigDecimal totalAmount = request.getDetails().stream()
                .map(d -> d.getPrice().multiply(new BigDecimal(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. 创建订单主表
        SaleOrder order = new SaleOrder();
        order.setOrderNo(orderNo);
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalQuantity(totalQuantity);
        order.setTotalAmount(totalAmount);
        order.setStatus(0);  // 草稿
        order.setRemark(request.getRemark());
        order.setCreateBy(userId);

        save(order);

        // 5. 创建订单明细
        List<SaleOrderDetail> details = new ArrayList<>();
        for (SaleOrderCreateRequest.OrderDetail dto : request.getDetails()) {
            SaleOrderDetail detail = new SaleOrderDetail();
            detail.setOrderId(order.getId());
            detail.setProductId(dto.getProductId());
            detail.setQuantity(dto.getQuantity());
            detail.setPrice(dto.getPrice());
            detail.setAmount(dto.getPrice().multiply(new BigDecimal(dto.getQuantity())));
            details.add(detail);
        }
        detailMapper.batchInsert(details);

        // 6. 锁定库存（草稿状态先锁定）
        for (SaleOrderCreateRequest.OrderDetail dto : request.getDetails()) {
            inventoryService.lockStock(dto.getProductId(), dto.getQuantity());
        }

        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Integer orderId, SaleOrderStatusRequest request, Integer userId) {
        SaleOrder order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("销售订单不存在");
        }

        Integer currentStatus = order.getStatus();
        Integer targetStatus = request.getStatus();

        if (!isValidStatusTransition(currentStatus, targetStatus)) {
            throw new RuntimeException("状态流转不合法");
        }

        // 审核通过：扣减库存
        if (targetStatus == 1) {
            List<SaleOrderDetail> details = detailMapper.selectByOrderId(orderId);
            for (SaleOrderDetail detail : details) {
                inventoryService.decreaseStock(
                        detail.getProductId(),
                        detail.getQuantity(),
                        "SALE_OUT",
                        orderId,
                        "销售出库，订单号：" + order.getOrderNo()
                );
            }
        }

        // 取消订单：释放锁定的库存
        if (targetStatus == 4) {
            List<SaleOrderDetail> details = detailMapper.selectByOrderId(orderId);
            for (SaleOrderDetail detail : details) {
                inventoryService.unlockStock(detail.getProductId(), detail.getQuantity());
            }
        }

        order.setStatus(targetStatus);
        order.setUpdateBy(userId);
        updateById(order);
    }

    // 其他方法实现类似采购订单...
}