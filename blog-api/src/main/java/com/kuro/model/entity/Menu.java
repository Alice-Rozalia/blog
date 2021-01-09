package com.kuro.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Table(name = "tb_menu")
@ApiModel(value="Menu对象", description="菜单表")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "菜单名称")
    @NotBlank(message = "菜单名称不能为空！")
    @NotNull(message = "菜单名称不能为空！")
    private String menuName;

    @ApiModelProperty(value = "菜单url")
    @NotBlank(message = "菜单url不能为空！")
    @NotNull(message = "菜单url不能为空！")
    private String path;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "上级菜单id")
    private Long parentId;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "类型 0菜单 1按钮")
    private Integer type;
}
