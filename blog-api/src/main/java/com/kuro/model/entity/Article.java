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
@Table(name = "tb_article")
@ToString
@ApiModel(value="Article对象", description="文章表")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "标题")
    @NotNull(message = "文章标题不能为空！")
    @NotBlank(message = "文章标题不能为空！")
    private String title;

    @ApiModelProperty(value = "内容")
    @NotNull(message = "文章内容不能为空！")
    @NotBlank(message = "文章内容不能为空！")
    private String content;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "浏览次数")
    private Long views;

    @ApiModelProperty(value = "是否为转载文章")
    private Boolean shareStatement;

    @ApiModelProperty(value = "是否发布")
    private Boolean published;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "分类id")
    private Long cid;

    @ApiModelProperty(value = "作者")
    private Long uid;
}
