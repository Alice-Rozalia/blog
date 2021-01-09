package com.kuro.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@ApiModel(value="User对象", description="用户表")
@Table(name = "tb_user")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "密码")
    @NotNull(message = "密码不能为空！")
    @NotBlank(message = "密码不能为空！")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ApiModelProperty(value = "手机号")
    @NotNull(message = "手机号不能为空！")
    @NotBlank(message = "手机号不能为空！")
    private String phone;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "更新时间")
    private Long modifiedTime;

    // 账号是否可用。默认为1（可用）
    @ApiModelProperty(value = "账号是否可用")
    private Boolean status;

    // 类型  0: 超级管理员，1：普通用户
    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "盐")
    @JsonIgnore
    private String salt;
}
