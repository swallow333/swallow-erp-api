package com.swalllow_erp.service.impl;


import com.swalllow_erp.entity.SysMenu;
import com.swalllow_erp.mapper.SysMenuMapper;
import com.swalllow_erp.mapper.SysUserRoleMapper;
import com.swalllow_erp.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 23:09
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<SysMenu> getUserMenus(Integer userId) {
        // 1. 查询用户的所有角色ID
        List<Integer> roleIds = sysUserRoleMapper.selectRoleIdsByUserId(userId);

        // 2. 查询这些角色下的所有菜单
        List<SysMenu> allMenus = new ArrayList<>();
        for (Integer roleId : roleIds) {
            List<SysMenu> menus = sysMenuMapper.selectMenusByRoleId(roleId);
            allMenus.addAll(menus);
        }

        // 3. 去重
        allMenus = allMenus.stream()
                .distinct()
                .collect(Collectors.toList());

        // 4. 构建树形结构
        return buildMenuTree(allMenus, 0);
    }

    @Override
    public List<String> getUserPermissions(Integer userId) {
        return sysMenuMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public List<SysMenu> getMenuTree() {
        List<SysMenu> all = sysMenuMapper.selectEnabledMenus();
        return buildMenuTree(all, 0);
    }

    private List<SysMenu> buildMenuTree(List<SysMenu> all, Integer parentId) {
        return all.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> {
                    List<SysMenu> children = buildMenuTree(all, menu.getId());
                    if (!children.isEmpty()) {
                        menu.setChildren(children);
                    }
                })
                .collect(Collectors.toList());
    }
}