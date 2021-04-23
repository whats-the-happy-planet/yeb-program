package com.xxxx.server.controller;

import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.utils.PictureUtil;
import com.xxxx.server.utils.QiniuUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@RestController
public class AdminInfoController {

    @Resource
    private IAdminService adminService;
//    @Resource
//    private QNService qnService;
//    @Value("${qiniu.path}")
//    private String path;

//    @ApiOperation(value = "更新用户头像")
//    @PostMapping("/admin/userface")
//    public RespBean updateAdminUserFace(@RequestParam("file") MultipartFile file, Integer id, Authentication authentication) throws IOException {
//        Response response = qnService.uploadFile(file.getInputStream());
//        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
//        String url = path + "/" +putRet.key;
//        return adminService.updateAdminUserFace(url,id,authentication);
//
//    }

    @ApiOperation(value = "更新用户头像")
    @PostMapping("/admin/userface")
    private RespBean updateAdminUserFace(@RequestParam("file") MultipartFile multipartFile,Integer id,Authentication authentication) throws IOException {
        // 用来获取其他参数
        if (!multipartFile.isEmpty()) {
            FileInputStream inputStream = (FileInputStream) multipartFile.getInputStream();
            String path = QiniuUtils.uploadQNImg(inputStream, PictureUtil.getRandomFileName()); // KeyUtil.genUniqueKey()生成图片的随机名
            System.out.print("七牛云返回的图片链接:" + path);
            return adminService.updateAdminUserFace(path,id,authentication);
        }
        return RespBean.error("文件不能为空");
    }
//    @ApiOperation(value = "更新用户头像")
//    @PostMapping("/admin/userface")
//    private String postUserInforUpDate(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile) throws IOException {
//        // 用来获取其他参数
//        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
//        String name = params.getParameter("username");
//        if (!multipartFile.isEmpty()) {
//            FileInputStream inputStream = (FileInputStream) multipartFile.getInputStream();
//            String path = QiniuUtils.uploadQNImg(inputStream, PictureUtil.getRandomFileName()); // KeyUtil.genUniqueKey()生成图片的随机名
//            System.out.print("七牛云返回的图片链接:" + path);
//            return path;
//        }
//        return "上传失败";
//    }


    @ApiOperation(value = "更新当前用户信息")
    @PutMapping("/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication) {
        if (adminService.updateAdmin(admin)==1) {
            SecurityContextHolder.getContext().setAuthentication
                    (new UsernamePasswordAuthenticationToken(admin, null, authentication.getAuthorities()));
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/admin/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String, Object> info) {
        String oldPass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updateAdminPassword(oldPass, pass, adminId);
    }


}
