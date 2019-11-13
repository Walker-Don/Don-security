package com.imooc.security.app;

import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.config.ValidateCodeSecurityConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author zhailiang
 */
@Configuration
@EnableResourceServer //资源服务器 focus 继承的类和browser是不一样的
public class ImoocResourceServerConfig extends ResourceServerConfigurerAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SecurityProperties securityProperties;

	//@Autowired
	//private UserDetailsService userDetailsService;
	//
	//@Autowired
	//@Qualifier("dataSource")
	//private DataSource dataSource;
	//
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;

	//@Autowired
	//private SpringSocialConfigurer imoocSocialSecurityConfig;

	@Autowired
	private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler imoocAuthenticationFailureHandler;

	//需要预先认证的页面会调用security配置
	@Override
	public void configure(HttpSecurity http) throws Exception {

		logger.warn("启动HttpSecurity配置");

		//封装passedUrls focus 这些url不能为null或者空
		String[] urlsInternal = {
				SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
				SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
				securityProperties.getBrowser().getSession().getSessionInvalidUrl(),
				securityProperties.getBrowser().getLoginPage(),//登陆页面
				securityProperties.getBrowser().getSignUpUrl(),//注册页面
				securityProperties.getBrowser().getLogoutSuccessPage(),//退出登陆成功页面
				SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*"};
		String[] urlsExternal = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getBrowser().getPassedUrls(), ",");

		String[] passedUrls = new String[urlsInternal.length + urlsExternal.length];
		System.arraycopy(urlsInternal, 0, passedUrls, 0, urlsInternal.length);
		System.arraycopy(urlsExternal, 0, passedUrls, urlsInternal.length, urlsExternal.length);

		applyPasswordAuthenticationConfig(http);

		//super.configure(http);
		http
				.apply(validateCodeSecurityConfig)
				.and()
				.apply(smsCodeAuthenticationSecurityConfig)
				//.and()
				//.apply(imoocSocialSecurityConfig)

				.and()
				.authorizeRequests()//需要请求授权
				.antMatchers(passedUrls)
				.permitAll()//跳过，不需要登陆，能够通过springSecurity自定义的filter，但是不一定能够通过自己定义的filter
				.anyRequest().authenticated()//任何请求都需要身份认证

				.and()
				.csrf().disable();//关闭csrf
	}

	private void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
				.loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
				.successHandler(imoocAuthenticationSuccessHandler)
				.failureHandler(imoocAuthenticationFailureHandler);
	}
}