package com.swalllow_erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.SaleOrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:31
 */

@Mapper
public interface SaleOrderDetailMapper extends BaseMapper<SaleOrderDetail> {

    /**
     * 根据订单ID查询明细
     */
    @Select("SELECT * FROM sale_order_detail WHERE order_id = #{orderId}")
    List<SaleOrderDetail> selectByOrderId(@Param("orderId") Integer orderId);

    /**
     * 批量插入明细
     */
    @Insert({
            "<script>",
            "INSERT INTO sale_order_detail (order_id, product_id, quantity, price, amount) VALUES",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.orderId}, #{item.productId}, #{item.quantity}, #{item.price}, #{item.amount})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int batchInsert(@Param("list") List<SaleOrderDetail> details);

    // 新增方法
    @Select("SELECT p.id AS productId, p.title AS productName, p.sku AS productSku, " +
            "SUM(d.quantity) AS totalQuantity, SUM(d.amount) AS totalAmount " +
            "FROM sale_order_detail d " +
            "JOIN product p ON d.product_id = p.id " +
            "JOIN sale_order o ON d.order_id = o.id " +
            "WHERE o.status IN (2, 3) " +
            "GROUP BY d.product_id " +
            "ORDER BY totalAmount DESC " +
            "LIMIT #{limit}")
    List<DashboardStatistics.ProductRank> selectTopProducts(@Param("limit") Integer limit);

    // SaleOrderDetailMapper.java
    @Select("SELECT p.id AS productId, p.title AS productName, p.sku AS productSku, " +
            "SUM(d.quantity) AS totalQuantity, SUM(d.amount) AS totalAmount " +
            "FROM sale_order_detail d " +
            "JOIN product p ON d.product_id = p.id " +
            "JOIN sale_order o ON d.order_id = o.id " +
            "WHERE o.status IN (2, 3) " +
            "GROUP BY d.product_id " +
            "ORDER BY totalAmount DESC " +
            "LIMIT #{limit}")
    List<Map<String, Object>> selectTopProducts(@Param("limit") Integer limit);
}