package com.swalllow_erp.dto.request;

import lombok.Data;
import java.util.List;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 23:06
 */

@Data
public class RoleUpdateRequest {
    private String roleName;
    private String description;
    private Integer status;
    private List<Integer> menuIds;
}