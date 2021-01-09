package com.kuro.auth;

import com.kuro.auth.jwt.JwtProperties;
import com.kuro.auth.jwt.JwtUtils;
import com.kuro.common.entity.ActiveUser;
import com.kuro.common.entity.ResultCode;
import com.kuro.common.entity.UserInfo;
import com.kuro.model.entity.Menu;
import com.kuro.model.entity.Role;
import com.kuro.model.entity.User;
import com.kuro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties prop;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();

        if (activeUser.getUser().getType() == 0) {
            authorizationInfo.addStringPermission("*:*");
        } else {
            List<String> permissions = new ArrayList<>(activeUser.getPermissions());
            List<Role> roleList = activeUser.getRoles();
            // 授权角色
            if (!CollectionUtils.isEmpty(roleList)) {
                for (Role role : roleList) {
                    authorizationInfo.addRole(role.getRoleName());
                }
            }
            // 授权权限
            if (!CollectionUtils.isEmpty(permissions)) {
                for (String permission : permissions) {
                    if (permission != null && !"".equals(permission)) {
                        authorizationInfo.addStringPermission(permission);
                    }
                }
            }
        }
        return authorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得 email，用于和数据库进行对比
        UserInfo userInfo = new UserInfo();
        try {
            userInfo = JwtUtils.getInfoFromToken(token, this.prop.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationException(ResultCode.USER_TOKEN_EXPIRED.getMessage());
        }
        String phone = userInfo.getPhone();
        if (phone == null) {
            throw new AuthenticationException(ResultCode.TOKEN_ERROR.getMessage());
        }
        User user = userService.findUserByPhone(phone);

        if (user == null) {
            throw new AccountException(ResultCode.USER_ACCOUNT_NOT_EXIST.getMessage());
        }

        if (!user.getStatus()) {
            throw new LockedAccountException(ResultCode.USER_ACCOUNT_LOCKED.getMessage());
        }

        // 如果验证通过，获取用户的角色
        List<Role> roles = userService.findRolesById(user.getId());
        // 查询用户的所有菜单(包括了菜单和按钮)
        List<Menu> menus = userService.findMenuByRoles(roles);

        Set<String> urls = new HashSet<>();
        Set<String> perms = new HashSet<>();
        if (!CollectionUtils.isEmpty(menus)) {
            for (Menu menu : menus) {
                String url = menu.getPath();
                String per = menu.getPerms();
                if (menu.getType() == 0 && !StringUtils.isEmpty(url)) {
                    urls.add(menu.getPath());
                }
                if (menu.getType() == 1 && !StringUtils.isEmpty(per)) {
                    perms.add(menu.getPerms());
                }
            }
        }
        // 过滤出url,和用户的权限
        ActiveUser activeUser = new ActiveUser();
        activeUser.setRoles(roles);
        activeUser.setUser(user);
        activeUser.setMenus(menus);
        activeUser.setUrls(urls);
        activeUser.setPermissions(perms);
        return new SimpleAuthenticationInfo(activeUser, token, getName());
    }
}
