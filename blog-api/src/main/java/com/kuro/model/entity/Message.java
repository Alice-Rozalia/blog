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
@Table(name = "tb_message")
@ApiModel(value="Message对象", description="留言表")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "留言内容")
    @NotNull(message = "留言内容不能为空！")
    @NotBlank(message = "留言内容不能为空！")
    private String content;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "留言时间")
    private Long time;

    // 父id，0为一级评论，其他值表示是评论的回复
    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "留言层级")
    private Integer level;
}
