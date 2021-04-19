package com.xxxx.server.config.compoent;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 权限控制
 * 判断用户角色
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {

	/**
     *
	 * @param authentication  用户对象
	 * @param object
     * @param configAttributes  当前访问资源地址对应的角色
	 * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
	 */
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		//遍历当前访问资源地址对应的所需要的所有角色
		for (ConfigAttribute configAttribute : configAttributes) {
			//当前资源对应的角色  Role_xxxx
			String needRole = configAttribute.getAttribute();
			//判断角色是否登录即可访问的角色，此角色在CustomFilter中设置
			if ("ROLE_LOGIN".equals(needRole)){
				//判断是否登录
				if (authentication instanceof AnonymousAuthenticationToken){
					throw new AccessDeniedException("尚未登录，请登录！");
				}else {
					return;
				}
			}
			//查询当前用户拥有的所有角色
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			//判断用户角色是否为 url所需角色
			for (GrantedAuthority authority : authorities) {
				if (authority.getAuthority().equals(needRole)){
					return;
				}
			}
		}
		throw new AccessDeniedException("权限不足，请联系管理员!");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}