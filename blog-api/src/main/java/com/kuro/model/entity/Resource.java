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

/**
 * 资源分享的实体类
 */
@Data
@Table(name = "tb_resource")
@ApiModel(value="Resource对象", description="资源表")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "资源名称")
    @NotNull(message = "资源名称不能为空！")
    @NotBlank(message = "资源名称不能为空！")
    private String name;

    @ApiModelProperty(value = "资源链接")
    @NotNull(message = "资源链接不能为空！")
    @NotBlank(message = "资源链接不能为空！")
    private String link;

    @ApiModelProperty(value = "提取码")
    @NotNull(message = "提取码不能为空！")
    @NotBlank(message = "提取码不能为空！")
    private String code;

    @ApiModelProperty(value = "资源分类")
    @NotNull(message = "资源分类不能为空！")
    @NotBlank(message = "资源分类不能为空！")
    private String type;

    @ApiModelProperty(value = "分享资源的用户的id")
    private Long userId;

    @ApiModelProperty(value = "资源图片")
    private String image;
}
