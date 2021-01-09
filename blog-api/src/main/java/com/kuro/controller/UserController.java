package com.kuro.controller;

import com.aliyuncs.exceptions.ClientException;
import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.model.entity.User;
import com.kuro.model.vo.MenuVo;
import com.kuro.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/v1")
@Api(value = "系统管理模块", tags = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 校验用户名或邮箱是否可用
     * @param data  要校验的值
     * @param type  （1,2）  1表示校验用户名，2表示校验邮箱
     * @return
     */
    @GetMapping("/pub/user/check/{data}/{type}")
    @ApiOperation(value = "注册检验", notes = "校验用户名或邮箱是否可用")
    public Result checkUser(
            @PathVariable("data") String data,
            @PathVariable("type") Integer type
    ) {
        Boolean result = this.userService.checkUser(data, type);
        if (result) {
            return Result.custom(ResultCode.PARAM_CAN_USE);
        }
        return Result.custom(type == 1 ? ResultCode.NICKNAME_ACCOUNT_ALREADY_EXIST : ResultCode.PHONE_ACCOUNT_ALREADY_EXIST);
    }

    @PostMapping("/pub/user/login")
    @ApiOperation(value = "登录授权", notes = "登录授权接口，登录成功返回token令牌")
    public Result login(@RequestBody User user, HttpServletRequest request) {
        if (StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getPassword())) {
            return Result.custom(ResultCode.PARAM_NOT_COMPLETE);
        }
        return this.userService.login(user.getPhone(), user.getPassword(), request);
    }

    @GetMapping("/pri/user/info")
    @ApiOperation(value = "用户信息", notes = "登录用户的信息")
    public Result info() {
        return Result.ok().data("userInfo", this.userService.info());
    }

    @GetMapping("/pri/user/list")
    @ApiOperation(value = "查询用户", notes = "分页查询所有的用户列表，不传参数则查询所有")
    public Result findUserList(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows
    ) {
        PageResult<User> result = this.userService.findUserList(key, page, rows);
        return Result.ok().data("users", result);
    }

    @GetMapping("/pri/user/find_menu")
    @RequiresPermissions({"menu:list"})
    @ApiOperation(value = "加载菜单", notes = "用户登录后, 根据角色加载菜单树")
    public Result findMenu() {
        List<MenuVo> menus = this.userService.findMenus();
        return Result.ok().data("menus", menus);
    }

    @GetMapping("/pub/user/code")
    @ApiOperation(value = "验证码", notes = "根据邮箱发送验证码，1为注册验证码，其余为改密验证码")
    public Result sendCode(
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "type", required = false) Integer type
    ) throws ClientException {
        return this.userService.sendCode(phone, type);
    }

    @PostMapping("/pub/user/register")
    @ApiOperation(value = "用户注册", notes = "注册新用户")
    public Result register(@RequestBody User user, @RequestParam(value = "code", required = true) String code) {
        return this.userService.register(user, code);
    }

    @PutMapping("/pri/user/status")
    @ApiOperation(value = "状态修改", notes = "修改用户的状态，启用or禁用")
    public Result changeStatus(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "status") Boolean status
    ) {
        return this.userService.updateStatus(id, status);
    }

    @PutMapping("/pub/user/change_password")
    @ApiOperation(value = "修改密码", notes = "根据邮箱及验证码修改用户密码")
    public Result changePassword(@RequestBody User user, @RequestParam(value = "code", required = true) String code) {
        return this.userService.changePassword(user, code);
    }
}
