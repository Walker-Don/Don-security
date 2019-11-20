package com.imooc.security.rbac;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * RbacService的默认实现，但是这里没有连接上数据库 focus
 *
 * @author zhailiang
 */
@Component("rbacService")
public class RbacServiceImpl implements RbacService {

	private AntPathMatcher antPathMatcher = new AntPathMatcher();

	/**
	 * 把用户的所有有权限的url和request中的url比较，返回boolean
	 * <p>
	 * 这里默认一律返回false
	 */
	@Override
	public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
		//1. 默认permission为null
		boolean hasPermission = false;

		//2. 从auth中拿出userDetails,如果认证成功会有一个
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {

			String username = ((UserDetails) principal).getUsername();
			//3. 读取用户所拥有权限的所有URL，根据用户--角色--资源
			//..数据库代码
			Set<String> urls = new HashSet<>();
			//逐个比较
			for (String url : urls) {
				if (antPathMatcher.match(url, request.getRequestURI())) {
					hasPermission = true;
					break;
				}
			}
		}
		//4. 返回
		return hasPermission;
	}

}
