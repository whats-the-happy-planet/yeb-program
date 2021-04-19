package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.AdminMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.utils.JwtTokenUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
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

    @Override
    public RespBean login(String username, String password) {
        //加载登录对象信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails == null || !passwordEncoder.matches(password,userDetails.getPassword())){
            throw new UsernameNotFoundException("用户名和密码异常");
        }

        //判断当前对象是否可用
        if(!userDetails.isEnabled()){
            return RespBean.error("用户状态异常");
        }

        //将用户的基本信息存放在security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //准备令牌
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, Object> map = new HashMap<>();
        System.out.println(tokenHead);
        map.put("tokenHead",tokenHead);
        map.put("token",token);
        return RespBean.success("登录成功",map);
    }

    /**
     * 根据用户名查询对象
     * @param name
     * @return
     */
    @Override
    public Admin quryAdminByName(String name) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", name));
    }

    /**
     * 根据用户信息查询对应角色
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
}
