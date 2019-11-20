package com.imooc.security;

import com.imooc.security.core.authorize.AuthorizeConfigProvider;
import com.imooc.security.rbac.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 设置demo模块的url的权限
 */
@Component
@Order(Integer.MIN_VALUE)
public class DemoAuthorizeConifgProvider implements AuthorizeConfigProvider {

	@Autowired
	private RbacService rbacService;

	@Override
	public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {

		//role角色大小要对的上
		//config.antMatchers("/user").hasRole("USER");

		//使用rbacService的权限表达式
		//config.anyRequest().access("@rbacService.hasPermission(request, authentication)");

	}

}
