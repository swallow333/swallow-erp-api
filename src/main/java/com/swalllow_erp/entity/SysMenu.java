package com.swalllow_erp.entity;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 23:05
 */
@Data
public class SysMenu {
    private Integer id;
    private String menuName;
    private Integer parentId;
    private Integer menuType;
    private String path;
    private String component;
    private String permission;
    private String icon;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 非数据库字段（树形结构用）
    private List<SysMenu> children;
}