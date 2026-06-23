package com.swalllow_erp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;



/**
 * @Author: Swallow333
 * @Date: 2026/06/23 23:08
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    @Select("SELECT menu_id FROM sys_role_menu WHERE role_id = #{roleId}")
    List<Integer> selectMenuIdsByRoleId(@Param("roleId") Integer roleId);
}