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
@Table(name = "tb_diary")
@ApiModel(value="Diary对象", description="日记表")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "内容")
    @NotNull(message = "内容不能为空！")
    @NotBlank(message = "内容不能为空！")
    private String content;

    @ApiModelProperty(value = "记录时间")
    private Long createTime;
}
