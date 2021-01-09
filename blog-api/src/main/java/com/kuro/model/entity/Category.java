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
@Table(name = "tb_category")
@ApiModel(value="Category对象", description="分类表")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "分类名")
    @NotBlank(message = "分类名不能为空！")
    @NotNull(message = "分类名不能为空！")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;
}
