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
 * 友情链接实体类
 */
@Data
@Table(name = "tb_link")
@ApiModel(value="Link对象", description="友情链接表")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "友链名")
    @NotBlank(message = "友链名不能为空！")
    @NotNull(message = "友链名不能为空！")
    private String name;

    @ApiModelProperty(value = "链接")
    @NotBlank(message = "链接不能为空！")
    @NotNull(message = "链接不能为空！")
    private String link;

    @ApiModelProperty(value = "图片")
    private String image;
}
