package com.kuro.service;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.model.entity.LoginLog;

public interface LoginLogService {

    /**
     * 分页查询登录日志
     * @param page
     * @param rows
     * @return
     */
    PageResult<LoginLog> findLoginLog(Integer page, Integer rows);

    /**
     * 根据 id 删除登录日志
     * @param id
     * @return
     */
    Result deleteLoginLog(Long id);
}
