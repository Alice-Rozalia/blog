package com.kuro.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@ApiModel(value="UserInfoVo对象", description="用户详情")
public class UserInfoVo {

    private String nickname;

    private String avatar;

    private Set<String> url;

    private List<String> roles;

    private Boolean isAdmin;

    private Set<String> perms;
}
