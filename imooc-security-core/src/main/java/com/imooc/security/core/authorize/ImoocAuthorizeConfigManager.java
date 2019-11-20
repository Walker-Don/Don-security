package com.imooc.security.core.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 管理项目所有的AuthorizeConfigProvider
 */
@Component
public class ImoocAuthorizeConfigManager implements AuthorizeConfigManager {

	@Autowired
	private List<AuthorizeConfigProvider> authorizeConfigProviders;//把所有的AuthorizeConfigProvider收集起来，并且有序调用

	@Override
	public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		for (AuthorizeConfigProvider authorizeConfigProvider : authorizeConfigProviders) {
			authorizeConfigProvider.config(config);
		}
		//最后设置其他的url都需要认证
		//	config.anyRequest().authenticated();
	}

}
