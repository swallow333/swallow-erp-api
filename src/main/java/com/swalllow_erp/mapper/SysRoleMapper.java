package com.swalllow_erp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 23:07
 */

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("SELECT * FROM sys_role WHERE role_code = #{roleCode}")
    SysRole findByCode(@Param("roleCode") String roleCode);
}