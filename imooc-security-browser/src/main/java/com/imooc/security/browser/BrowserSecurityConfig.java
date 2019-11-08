package com.imooc.security.browser;

import com.imooc.security.core.authentication.AbstractChannelSecurityConfig;
import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.config.ValidateCodeSecurityConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * BrowserSecurityConfig配置类，
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName BrowserSecurityConfig
 * @date 2019年10月09日 上午 11:32
 */
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	@Autowired
	private PersistentTokenRepository persistentTokenRepository;

	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;

	@Autowired
	private SpringSocialConfigurer imoocSocialSecurityConfig;

	@Autowired
	private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

	@Autowired
	private InvalidSessionStrategy invalidSessionStrategy;

	//PersistentTokenRepository 记住我功能
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		//tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}

	//PasswordEncoder
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//需要预先认证的页面会调用security配置
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		logger.warn("启动HttpSecurity配置");

		//封装passedUrls
		String[] urlsInternal = {
				SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
				SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
				securityProperties.getBrowser().getSession().getSessionInvalidUrl(),
				securityProperties.getBrowser().getLoginPage(),//登陆页面
				securityProperties.getBrowser().getSignUpUrl(),//注册页面
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
				.and()
				.apply(imoocSocialSecurityConfig)

				.and()
				.sessionManagement()
				//InvalidSessionId，时间过长
				.invalidSessionStrategy(invalidSessionStrategy)//使用新的strategy
				//.invalidSessionUrl(securityProperties.getBrowser().getSession().getSessionInvalidUrl()) //使用默认的sessionInvalid_url

				//This session has been expired (possibly due to multiple concurrent logins being attempted as the same user).
				.maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())//1，后面的session会把前面的session踢掉
				.maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())//达到最大session后禁止别处登陆，先下线
				.expiredSessionStrategy(sessionInformationExpiredStrategy)//并发登陆导致系统session登掉的策略
				.and()//两个and()?

				.and()
				.rememberMe()
				.alwaysRemember(securityProperties.getBrowser().isAlwaysRemember())
				.tokenRepository(persistentTokenRepository)  // .tokenRepository(persistentTokenRepository())视频重新new一个，何必bean‘
				.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
				.userDetailsService(userDetailsService)

				.and()
				.authorizeRequests()//需要请求授权
				.antMatchers(passedUrls)
				.permitAll()//跳过，不需要登陆，能够通过springSecurity自定义的filter，但是不一定能够通过自己定义的filter
				.anyRequest().authenticated()//任何请求都需要身份认证

				.and()
				.csrf().disable();//关闭csrf
	}

}
