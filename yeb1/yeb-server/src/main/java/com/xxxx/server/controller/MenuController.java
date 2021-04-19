package com.xxxx.server.controller;


import com.xxxx.server.pojo.Menu;
import com.xxxx.server.service.impl.MenuServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
@RestController
@RequestMapping("/system")
public class MenuController {

    @Resource
    private MenuServiceImpl menuService;

    @ApiOperation(value = "加载当前用户所拥有的资源")
    @GetMapping("menu")
    public List<Menu> getMenuByUserName(){
        return menuService.getMenuByAdminName();
    }

}
