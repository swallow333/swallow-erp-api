package com.swalllow_erp.service;



import com.swalllow_erp.entity.SysMenu;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 23:08
 */

public interface SysMenuService {

    /**
     * 获取用户菜单树
     */
    List<SysMenu> getUserMenus(Integer userId);

    /**
     * 获取用户权限标识列表
     */
    List<String> getUserPermissions(Integer userId);

    /**
     * 获取所有菜单树
     */
    List<SysMenu> getMenuTree();
}