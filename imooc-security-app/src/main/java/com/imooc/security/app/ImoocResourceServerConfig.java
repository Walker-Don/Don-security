package com.imooc.security.app;

import com.imooc.security.app.authentication.oponid.OpenIdAuthenticationSecurityConfig;
import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.authorize.AuthorizeConfigManager;
import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.config.ValidateCodeSecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

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

	@Autowired
	private SpringSocialConfigurer imoocSocialSecurityConfig; //social登陆的配置

	@Autowired
	private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

	@Autowired
	private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler imoocAuthenticationFailureHandler;

	@Autowired
	private AuthorizeConfigManager authorizeConfigManager;//

	//需要预先认证的页面会调用security配置
	@Override
	public void configure(HttpSecurity http) throws Exception {

		logger.warn("启动HttpSecurity配置");

		applyPasswordAuthenticationConfig(http);

		http
				.apply(validateCodeSecurityConfig)//验证码的filter
				.and()
				.apply(smsCodeAuthenticationSecurityConfig)//手机登陆的filter
				.and()
				.apply(openIdAuthenticationSecurityConfig)//用户通过app以openId来登陆的filter
				.and()
				.apply(imoocSocialSecurityConfig)//social登陆的配置，并且不会被拦截

				.and()
				.csrf().disable();//关闭csrf

		//引入url授权配置
		authorizeConfigManager.config(http.authorizeRequests());
	}

	private void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
				.loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
				.successHandler(imoocAuthenticationSuccessHandler)
				.failureHandler(imoocAuthenticationFailureHandler);
	}

	@Autowired
	private OAuth2WebSecurityExpressionHandler expressionHandler;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.expressionHandler(expressionHandler);
	}
}