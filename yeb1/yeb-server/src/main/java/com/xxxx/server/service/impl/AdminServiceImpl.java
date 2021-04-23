package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.AdminMapper;
import com.xxxx.server.mapper.AdminRoleMapper;
import com.xxxx.server.mapper.RoleMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.AdminRole;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private AdminMapper adminMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private AdminRoleMapper adminRoleMapper;

    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        String captcha = (String) request.getSession().getAttribute("captcha");
        if (StringUtils.isEmpty(code) || !captcha.equalsIgnoreCase(code)) {
            return RespBean.error("验证码错误");
        }
        //加载登录对象信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UsernameNotFoundException("用户名和密码异常");
        }

        //判断当前对象是否可用
        if (!userDetails.isEnabled()) {
            return RespBean.error("用户状态异常");
        }

        //将用户的基本信息存放在security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //准备令牌
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, Object> map = new HashMap<>();
        System.out.println(tokenHead);
        map.put("tokenHead", tokenHead);
        map.put("token", token);
        return RespBean.success("登录成功", map);
    }

    /**
     * 根据用户名查询对象
     *
     * @param name
     * @return
     */
    @Override
    public Admin quryAdminByName(String name) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", name));
    }

    /**
     * 根据用户信息查询对应角色
     *
     * @return
     */
    @Override
    public List<Admin> quryRoleByAdmin(Integer id) {
        // //获取当前登录用户信息
        // Admin principal = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Integer id = principal.getId();
        //查询对应的角色
        List<Admin> adminList = adminMapper.quryRoleByAdmin(id);
        return adminList;
    }

    //根据用户ID查询所有角色
    @Override
    public List<Role> quryRoles(Integer id) {
        return adminMapper.quryRoles(id);
    }

    @Override
    public List<Admin> queryAdminByUserName(String keywords) {
        return adminMapper.queryAdminByUserName(AdminUtils.getCurrentAdmin().getId(),keywords);
    }

    @Override
    public RespBean updateAdminUserFace(String url, Integer id, Authentication authentication) {
        Admin admin = adminMapper.selectById(id);
        admin.setUserFace(url);
        int result = adminMapper.updateById(admin);
        if(1==result){
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin,null,authentication.getAuthorities()));
            return RespBean.success("更新成功",url);
        }

        return RespBean.error("更新失败");
    }

    //通过操作员的id和角色id来对某个操作员的角色修改
    @Override
    @Transactional
    public RespBean updateRole(Integer adminId, Integer[] rids) {
        QueryWrapper<AdminRole> wrapper = new QueryWrapper<>();
        wrapper.eq("adminId", adminId);
        adminRoleMapper.delete(wrapper);
        Integer count = adminRoleMapper.updateRole(adminId, rids);
        if(rids.length != count){
               return RespBean.error("更新失败");
        }
        return RespBean.success("更新成功");
    }


    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).eq("enabled",true));
    }

    @Override
    public List<Role> getRolesByAdminId(Integer adminId) {
        return roleMapper.getRolesByAdminId(adminId);
    }

    /**
     * 更新当前用户信息
     *
     * @param admin
     * @return
     */
    @Override
    public Integer updateAdmin(Admin admin) {
        AssertUtil.isTrue(admin == null, "数据异常");
        AssertUtil.isTrue(adminMapper.selectById(admin.getId()) == null, "数据异常");

        Admin dbAdmin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", admin.getUsername()));
        AssertUtil.isTrue(dbAdmin != null && dbAdmin.getId() != admin.getId(), "用户名已存在");


        AssertUtil.isTrue(org.apache.commons.lang3.StringUtils.isBlank(admin.getPhone()), "用户联系手机不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(admin.getPhone()), "用户手机格式输入错误");

        AssertUtil.isTrue(adminMapper.updateById(admin) < 1, "更新失败");

        return adminMapper.updateById(admin);
    }

    @Override
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        AssertUtil.isTrue(oldPass == null, "旧密码不能为空");
        AssertUtil.isTrue(pass == null, "新密码不能为空");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPass, admin.getPassword())) {
            admin.setPassword(encoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if (result == 1) {
                return RespBean.success("更新密码成功");
            }
        }
        return RespBean.error("更新密码失败");
    }

    @Override
    public List<Admin> getAllAdmins(String keywords) {
        Integer id = ((Admin)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return adminMapper.getAllAdmins(id,keywords);
    }
}
