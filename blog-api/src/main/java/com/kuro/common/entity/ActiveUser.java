package com.kuro.common.entity;

import com.kuro.model.entity.Menu;
import com.kuro.model.entity.Role;
import com.kuro.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveUser {
    // 当前用户对象
    private User user;
    // 当前用户具有的角色
    private List<Role> roles;
    // 当前用户具有的url
    private Set<String> urls;
    // 包括 url + permission
    private List<Menu> menus;
    // 当前用户具有的权限API:例如[user:add], [user:delete]
    private Set<String> permissions;
    // 用户主机地址
    private String host;
    // 用户登录时系统 IP
    private String systemHost;
    // 状态
    private String status;
    // 超时时间
    private Long timeout;
    // 所在地
    private String location;
    // 是否为当前登录用户
    private Boolean current;
}
