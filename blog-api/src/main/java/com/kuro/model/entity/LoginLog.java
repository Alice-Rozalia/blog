package com.kuro.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "tb_login_log")
@ApiModel(value="LoginLog对象", description="登录日志表")
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "登录用户的昵称")
    private String nickname;

    @ApiModelProperty(value = "登录时间")
    private Long loginTime;

    @ApiModelProperty(value = "登录地址")
    private String location;

    @ApiModelProperty(value = "主机ip")
    private String ip;

    @ApiModelProperty(value = "系统")
    private String userSystem;

    @ApiModelProperty(value = "浏览器")
    private String userBrowser;

}
