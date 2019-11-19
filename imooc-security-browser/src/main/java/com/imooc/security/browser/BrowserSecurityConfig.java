package com.imooc.security.browser;

import com.imooc.security.core.authentication.AbstractChannelSecurityConfig;
import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.authorize.AuthorizeConfigManager;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.config.ValidateCodeSecurityConfig;
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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
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

	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler; //处理logout成功的handler

	@Autowired
	private AuthorizeConfigManager authorizeConfigManager;//http配置url授权的管理类

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

		applyPasswordAuthenticationConfig(http);

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
				.logout()
				.logoutUrl("/myLogout") //重新定义自己的logout接口地址
				//.logoutSuccessUrl("/imooc-logout-success.html")//自定义logout成功后的重定向地址，需要放行
				.logoutSuccessHandler(logoutSuccessHandler)
				.deleteCookies("JSESSIONID")//同时删除某些cookie

				.and()
				.csrf().disable();//关闭csrf

		//引入url授权配置
		authorizeConfigManager.config(http.authorizeRequests());
	}

}
