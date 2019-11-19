package com.imooc.security;

import com.imooc.security.core.authorize.AuthorizeConfigProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 设置demo模块的url的权限
 */
@Component
public class DemoAuthorizeConifgProvider implements AuthorizeConfigProvider {

	@Override
	public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {

		//role角色大小要对的上
		config.antMatchers("/user").hasRole("USER");
	}

}
