package com.kuro.service;


import com.aliyuncs.exceptions.ClientException;
import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.model.entity.Menu;
import com.kuro.model.entity.Role;
import com.kuro.model.entity.User;
import com.kuro.model.vo.MenuVo;
import com.kuro.model.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    /**
     * 分页查询用户列表，不传参数默认查询所有
     * @param key
     * @param page
     * @param rows
     * @return
     */
    PageResult<User> findUserList(String key, Integer page, Integer rows);

    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    User findUserByPhone(String phone);

    /**
     * 查询用户角色
     * @param id
     * @return
     */
    List<Role> findRolesById(Long id);

    /**
     * 根据用户角色查询用户的菜单
     * @param roles
     * @return
     */
    List<Menu> findMenuByRoles(List<Role> roles);

    // 获取菜单列表
    List<MenuVo> findMenus();

    /**
     * 登录
     * @param phone
     * @param password
     * @return
     */
    Result login(String phone, String password, HttpServletRequest request);

    // 获取用户详情
    UserInfoVo info();

    // 发送验证码
    Result sendCode(String phone, Integer type) throws ClientException;

    // 注册
    Result register(User user, String code);

    // 更新用户状态
    Result updateStatus(Long id, Boolean status);

    /**
     * 校验用户名或手机号是否可用
     * @param data  要校验的值
     * @param type  （1,2）  1表示校验用户名，2表示校验手机号
     * @return
     */
    Boolean checkUser(String data, Integer type);

    Result changePassword(User user, String code);
}
