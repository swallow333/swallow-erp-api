package com.swalllow_erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swalllow_erp.entity.Inventory;
import com.swalllow_erp.entity.StockFlow;
import com.swalllow_erp.entity.Product;
import com.swalllow_erp.mapper.InventoryMapper;
import com.swalllow_erp.mapper.StockFlowMapper;
import com.swalllow_erp.mapper.ProductMapper;
import com.swalllow_erp.service.InventoryService;
import com.swalllow_erp.dto.request.InventoryQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:12
 */

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory>
        implements InventoryService {

    @Autowired
    private StockFlowMapper stockFlowMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public PageInfo<Inventory> queryPage(InventoryQueryRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();

        // 如果有关键词，先查出匹配的商品ID
        if (StringUtils.hasText(request.getKeyword())) {
            List<Integer> productIds = productMapper.selectByKeyword(request.getKeyword())
                    .stream().map(Product::getId).collect(Collectors.toList());
            if (productIds.isEmpty()) {
                return new PageInfo<>(List.of());
            }
            wrapper.in(Inventory::getProductId, productIds);
        }

        // 库存范围筛选
        if (request.getMinStock() != null) {
            wrapper.ge(Inventory::getQuantity, request.getMinStock());
        }
        if (request.getMaxStock() != null) {
            wrapper.le(Inventory::getQuantity, request.getMaxStock());
        }

        // 只显示低库存
        if (request.getOnlyLowStock() != null && request.getOnlyLowStock()) {
            wrapper.lt(Inventory::getQuantity, 10);  // 安全库存阈值
        }

        wrapper.orderByDesc(Inventory::getUpdateTime);

        List<Inventory> list = list(wrapper);

        // 补充商品信息
        for (Inventory inv : list) {
            Product product = productMapper.selectById(inv.getProductId());
            if (product != null) {
                inv.setProductName(product.getTitle());
                inv.setProductSku(product.getSku());
            }
        }

        return new PageInfo<>(list);
    }

    @Override
    public Inventory getByProductId(Integer productId) {
        return baseMapper.selectByProductId(productId);
    }

    @Override
    public PageInfo<StockFlow> queryFlows(Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<StockFlow> wrapper = new LambdaQueryWrapper<>();
        if (productId != null) {
            wrapper.eq(StockFlow::getProductId, productId);
        }
        wrapper.orderByDesc(StockFlow::getCreateTime);
        List<StockFlow> list = stockFlowMapper.selectList(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseStock(Integer productId, Integer quantity, String sourceType, Integer sourceId, String remark) {
        Inventory inventory = baseMapper.selectByProductId(productId);
        if (inventory == null) {
            inventory = new Inventory();
            inventory.setProductId(productId);
            inventory.setQuantity(0);
            inventory.setLockedQuantity(0);
            inventory.setAvailableQuantity(0);
            baseMapper.insert(inventory);
        }

        int before = inventory.getQuantity();
        int after = before + quantity;
        inventory.setQuantity(after);
        inventory.setAvailableQuantity(after - inventory.getLockedQuantity());
        baseMapper.updateById(inventory);

        // 记录流水
        StockFlow flow = new StockFlow();
        flow.setProductId(productId);
        flow.setFlowType(1);
        flow.setChangeQuantity(quantity);
        flow.setBeforeQuantity(before);
        flow.setAfterQuantity(after);
        flow.setSourceType(sourceType);
        flow.setSourceId(sourceId);
        flow.setRemark(remark);
        stockFlowMapper.insert(flow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decreaseStock(Integer productId, Integer quantity, String sourceType, Integer sourceId, String remark) {
        Inventory inventory = baseMapper.selectByProductId(productId);
        if (inventory == null) {
            throw new RuntimeException("商品库存不存在");
        }
        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("库存不足");
        }

        int before = inventory.getQuantity();
        int after = before - quantity;
        inventory.setQuantity(after);
        inventory.setAvailableQuantity(after - inventory.getLockedQuantity());
        baseMapper.updateById(inventory);

        StockFlow flow = new StockFlow();
        flow.setProductId(productId);
        flow.setFlowType(2);
        flow.setChangeQuantity(-quantity);
        flow.setBeforeQuantity(before);
        flow.setAfterQuantity(after);
        flow.setSourceType(sourceType);
        flow.setSourceId(sourceId);
        flow.setRemark(remark);
        stockFlowMapper.insert(flow);
    }

    @Override
    public void lockStock(Integer productId, Integer quantity) {
        Inventory inventory = baseMapper.selectByProductId(productId);
        if (inventory == null || inventory.getAvailableQuantity() < quantity) {
            throw new RuntimeException("库存不足，无法锁定");
        }
        inventory.setLockedQuantity(inventory.getLockedQuantity() + quantity);
        inventory.setAvailableQuantity(inventory.getQuantity() - inventory.getLockedQuantity());
        baseMapper.updateById(inventory);
    }

    @Override
    public void unlockStock(Integer productId, Integer quantity) {
        Inventory inventory = baseMapper.selectByProductId(productId);
        if (inventory == null) {
            return;
        }
        inventory.setLockedQuantity(Math.max(0, inventory.getLockedQuantity() - quantity));
        inventory.setAvailableQuantity(inventory.getQuantity() - inventory.getLockedQuantity());
        baseMapper.updateById(inventory);
    }
}