package com.kuro.controller;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.model.entity.LoginLog;
import com.kuro.service.impl.LoginLogServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@Api(value = "系统管理模块", tags = "日志管理")
public class LogController {

    @Autowired
    private LoginLogServiceImpl loginLogService;

    @GetMapping("/pri/log/login")
    @ApiOperation(value = "登录日志", notes = "分页查询所有的登录日志，不传参数则查询所有")
    public Result findLoginLogs(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows
    ) {
        PageResult<LoginLog> loginLogs = this.loginLogService.findLoginLog(page, rows);
        return Result.ok().data("loginLogs", loginLogs);
    }

    @DeleteMapping("/pri/log/delete")
    @ApiOperation(value = "删除日志", notes = "根据登录日志的id删除登录日志")
    public Result deleteLoginLogById(@RequestParam(value = "id") Long id) {
        return this.loginLogService.deleteLoginLog(id);
    }
}
