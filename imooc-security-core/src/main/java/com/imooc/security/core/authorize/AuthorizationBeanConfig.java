package com.imooc.security.core.authorize;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;

/**
 * 异常补充bean
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年11月20日 下午 1:59
 */
@Configuration
public class AuthorizationBeanConfig {

	/**
	 * WebSecurityExpressionHandler 为空，需要自己的显式设置一个，并且在ResourceServerConfigurerAdapter中configure
	 *
	 * @param applicationContext 自动注入依赖
	 */
	@Bean
	public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
		OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
		expressionHandler.setApplicationContext(applicationContext);
		return expressionHandler;
	}
}
