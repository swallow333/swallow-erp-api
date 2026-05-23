package com.swalllow_erp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swalllow_erp.entity.SysUser;
import com.swalllow_erp.mapper.SysUserMapper;
import com.swalllow_erp.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public SysUser getByUsername(String username) {
        // 构造查询条件：username = 传入的值
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username); // 方法引用，最终得到一个SQL条件
        // 执行查询，根据Wrapper，查询一条记录，间接调用baseMapper方法
        return this.getOne(wrapper);
    }
}