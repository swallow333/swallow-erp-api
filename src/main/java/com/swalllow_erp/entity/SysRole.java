package com.swalllow_erp.entity;



import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 23:04
 */

@Data
public class SysRole {
    private Integer id;
    private String roleCode;
    private String roleName;
    private String description;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 非数据库字段
    private List<Integer> menuIds;
}