package com.xxxx.server.config.compoent;

import com.xxxx.server.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthencationTokenFilter  extends OncePerRequestFilter {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader; //Authrization
    @Value("${jwt.tokenHead}")
    private String tokenHead; //Bearer
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //拦截请求，校验令牌
        String authrization = request.getHeader(tokenHeader);
        if(authrization != null && authrization.startsWith(tokenHead)){
            //截取到具体的token数据
            String token = authrization.substring(tokenHead.length());
            //解析token
            String userName = jwtTokenUtil.getUserNameFromToken(token);

            if(userName != null && null == SecurityContextHolder.getContext().getAuthentication()){
                //登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                if(jwtTokenUtil.validateToken(token,userDetails)){
                    //将数据存放在后台security的上下文环境中，因为后期的访问接口时可以拿到当前登录用户的信息
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            }
        }
        filterChain.doFilter(request,response);
    }
}
