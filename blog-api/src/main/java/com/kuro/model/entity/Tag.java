package com.kuro.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Table(name = "tb_tag")
@ApiModel(value="Tag对象", description="标签表")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "标签名")
    @NotNull(message = "标签名不能为空！")
    @NotBlank(message = "标签名不能为空！")
    private String name;

    @ApiModelProperty(value = "颜色")
    @NotNull(message = "颜色不能为空！")
    @NotBlank(message = "颜色不能为空！")
    private String color;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;
}
