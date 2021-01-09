package com.kuro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.mapper.LoginLogMapper;
import com.kuro.model.entity.LoginLog;
import com.kuro.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public PageResult<LoginLog> findLoginLog(Integer page, Integer rows) {
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }
        List<LoginLog> loginLogs = this.loginLogMapper.selectAll();
        PageInfo<LoginLog> pageInfo = new PageInfo<>(loginLogs);
        return new PageResult<>(pageInfo.getTotal(), loginLogs);
    }

    /**
     * 根据 id 删除登录日志
     * @param id
     * @return
     */
    @Override
    public Result deleteLoginLog(Long id) {
        int result = this.loginLogMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            return Result.custom(ResultCode.DELETE_ERROR);
        }
        return Result.custom(ResultCode.DELETE_SECCESS);
    }
}
