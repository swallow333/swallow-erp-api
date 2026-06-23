package com.swalllow_erp.dto.request;



import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.List;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 23:05
 */

@Data
public class RoleCreateRequest {
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    private String description;
    private List<Integer> menuIds;  // 关联的菜单ID列表
}