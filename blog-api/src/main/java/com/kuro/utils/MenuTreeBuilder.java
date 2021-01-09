package com.kuro.utils;

import com.kuro.model.entity.Menu;
import com.kuro.model.vo.MenuVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建树形菜单
 */
public class MenuTreeBuilder {

    public static List<MenuVo> build(List<Menu> menus) {
        List<MenuVo> menuVos = new ArrayList<>();
        // 获取父节点
        for (Menu menu : menus) {
            if (menu.getParentId() == null) {
                continue;
            }
            if (menu.getParentId() == 0L) {
                MenuVo menuBo = new MenuVo();
                BeanUtils.copyProperties(menu, menuBo);
                menuVos.add(menuBo);
            }
        }
        // 组装子节点
        for (MenuVo menuVo : menuVos) {
            List<Menu> child = new ArrayList<>();
            for (Menu menu : menus) {
                if (menu.getParentId() == menuVo.getId()) {
                    child.add(menu);
                }
            }
            menuVo.setChildren(child);
        }
        return menuVos;
    }
}
