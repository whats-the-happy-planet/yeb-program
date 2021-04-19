package com.xxxx.server.controller;


import com.xxxx.server.pojo.Joblevel;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
@RestController
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {
    @Resource
    private IJoblevelService joblevelService;

    @ApiOperation(value = "显示所有职称")
    @GetMapping("/")
    protected List<Joblevel> getAllJobLevels() {
        return joblevelService.list();
    }

    @ApiOperation(value = "添加职称")
    @PostMapping("/")
    public RespBean addJobLevel(Joblevel joblevel) {
        joblevelService.addJobLevel(joblevel);
        return RespBean.success("添加职称成功");
    }

    @ApiOperation(value = "更新职称")
    @PutMapping("/")
    public RespBean updateJobLevel(Joblevel joblevel) {
        joblevelService.updateJobLevel(joblevel);
        return RespBean.success("更新职称成功");
    }

    @ApiOperation(value = "删除职称")
    @DeleteMapping("/{id}")
    public RespBean deleteJobLevel(@PathVariable Integer id) {
        joblevelService.deleteJobLevel(id);
        return RespBean.success("删除职称成功");
    }

    @ApiOperation(value = "批量删除职称")
    @DeleteMapping("/")
    public RespBean deleteJobLevels(Integer[] ids) {
        joblevelService.deleteJobLevels(ids);
        return RespBean.success("批量删除职称成功");
    }
}
