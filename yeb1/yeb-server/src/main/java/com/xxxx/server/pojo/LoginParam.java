package com.xxxx.server.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="登录参数对象", description="")
public class LoginParam {
    @ApiModelProperty(value = "用户名",required = true)
    private String userName;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
}
