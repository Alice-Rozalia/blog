package com.kuro.mapper;

import com.kuro.model.entity.Menu;
import com.kuro.model.entity.Role;
import com.kuro.model.entity.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    /**
     * 查询用户角色
     * @param id
     * @return
     */
    List<Role> findRolesById(@Param("user_id") Long id);

    /**
     * 查询用户所拥有的菜单列表
     * @param id
     * @return
     */
    List<Menu> findMenuByRoleId(@Param("role_id") Long id);
}
