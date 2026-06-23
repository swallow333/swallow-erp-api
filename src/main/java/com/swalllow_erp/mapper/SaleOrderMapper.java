package com.swalllow_erp.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.SaleOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:31
 */

@Mapper
public interface SaleOrderMapper extends BaseMapper<SaleOrder> {

    /**
     * 根据订单编号查询
     */
    @Select("SELECT * FROM sale_order WHERE order_no = #{orderNo}")
    SaleOrder findByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 统计今日订单数量（用于生成单号）
     */
    @Select("SELECT COUNT(*) FROM sale_order WHERE order_no LIKE CONCAT(#{date}, '%')")
    int countTodayOrders(@Param("date") String date);

    // 新增以下方法

    /**
     * 统计总销售额（按状态）
     */
    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM sale_order WHERE status IN (${statuses})")
    BigDecimal sumTotalAmountByStatuses(@Param("statuses") List<Integer> statuses);

    /**
     * 按日期统计销售额
     */
    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM sale_order " +
            "WHERE DATE(create_time) = #{date} AND status IN (2, 3)")
    BigDecimal sumTotalAmountByDate(@Param("date") String date);

    /**
     * 按日期统计订单数
     */
    @Select("SELECT COUNT(*) FROM sale_order WHERE DATE(create_time) = #{date}")
    Integer countByDate(@Param("date") String date);

    /**
     * 按状态统计订单数
     */
    @Select("SELECT COUNT(*) FROM sale_order WHERE status = #{status}")
    Integer countByStatus(@Param("status") Integer status);

    /**
     * 按状态列表统计订单数
     */
    @Select("SELECT COUNT(*) FROM sale_order WHERE status IN (${statuses})")
    Integer countByStatuses(@Param("statuses") List<Integer> statuses);

    /**
     * 查询近期订单动态
     */
    @Select("SELECT order_no, customer_name, total_amount, status, create_time " +
            "FROM sale_order " +
            "ORDER BY create_time DESC LIMIT #{limit}")
    List<DashboardStatistics.OrderActivity> selectRecentActivities(@Param("limit") Integer limit);
}