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
@Table(name = "tb_comment")
@ApiModel(value="Comment对象", description="评论表")
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "评论的文章的id")
    @NotNull(message = "文章id不能为空！")
    private Long aid;

    @ApiModelProperty(value = "评论的用户的id")
    private Long uid;

    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "评论内容不能为空！")
    @NotNull(message = "评论内容不能为空！")
    private String content;

    @ApiModelProperty(value = "评论时间")
    private Long commentTime;

    // 父id，0为一级评论，其他值表示是评论的回复
    @ApiModelProperty(value = "父id")
    private Long parentId;
}
