package com.swalllow_erp.dto.request;



import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 23:06
 */
@Data
public class UserRoleAssignRequest {
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotNull(message = "角色ID列表不能为空")
    private List<Integer> roleIds;
}