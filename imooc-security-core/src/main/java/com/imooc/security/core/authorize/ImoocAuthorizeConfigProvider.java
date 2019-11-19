package com.imooc.security.core.authorize;

import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 默认核心模块的url权限配置，基本都不需要权限和认证
 */
@Component
@Order(Integer.MIN_VALUE)
public class ImoocAuthorizeConfigProvider implements AuthorizeConfigProvider {

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {

		//1. 封装passedUrls focus 这些url不能为null或者空
		String[] urlsInternal = {
				SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
				SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
				SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_OPENID,//openId filter的url
				securityProperties.getBrowser().getSession().getSessionInvalidUrl(),
				securityProperties.getBrowser().getLoginPage(),//登陆页面
				securityProperties.getBrowser().getSignUpUrl(),//注册页面
				securityProperties.getBrowser().getLogoutSuccessPage(),//退出登陆成功页面
				securityProperties.getBrowser().getSignOutUrl(),
				SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
				"/js/*", "/social/signUp", "/user/regist", "/static/js/*"
		};
		String[] urlsExternal = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getBrowser().getPassedUrls(), ",");

		String[] passedUrls = new String[urlsInternal.length + urlsExternal.length];
		System.arraycopy(urlsInternal, 0, passedUrls, 0, urlsInternal.length);
		System.arraycopy(urlsExternal, 0, passedUrls, urlsInternal.length, urlsExternal.length);

		//2. passedUrls都放行
		config.antMatchers(passedUrls).permitAll();
	}

}
