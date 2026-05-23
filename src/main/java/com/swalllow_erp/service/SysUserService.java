package com.swalllow_erp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swalllow_erp.entity.SysUser;

    // 继承IService接口，封装了常见的CRUD操作
public interface SysUserService extends IService<SysUser> {
    // 自定义抽象方法：根据用户名查询用户，在实现类实现
    SysUser getByUsername(String username);
}