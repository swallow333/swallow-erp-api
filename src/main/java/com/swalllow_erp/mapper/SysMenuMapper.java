package com.swalllow_erp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 23:07
 */

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    @Select("SELECT * FROM sys_menu WHERE status = 1 ORDER BY sort_order")
    List<SysMenu> selectEnabledMenus();

    @Select("SELECT m.* FROM sys_menu m " +
            "JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId} AND m.status = 1 " +
            "ORDER BY m.sort_order")
    List<SysMenu> selectMenusByRoleId(@Param("roleId") Integer roleId);

    @Select("SELECT DISTINCT m.permission FROM sys_menu m " +
            "JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.permission IS NOT NULL")
    List<String> selectPermissionsByUserId(@Param("userId") Integer userId);
}