/**
 *
 */
package com.imooc.security.core.social;

import com.imooc.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 配置social登陆相关的各种bean
 *
 * @author zhailiang
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	@Autowired(required = false)//不一定需要，不提供就不用，对应下面
	private ConnectionSignUp connectionSignUp;

	@Autowired(required = false)//给SpringSocialConfigurer传入一个后续处理器，看具体实现
	private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

	//顺便在数据库创建table，不使用InMemoryUsersConnectionRepository
	@Override
	@Bean
	@Primary
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
		repository.setTablePrefix("imooc_");
		//隐式注册，不需要客户主动注册
		if (connectionSignUp != null) {
			repository.setConnectionSignUp(connectionSignUp);
		}
		return repository;
	}

	//https://segmentfault.com/q/1010000014110898?utm_source=tag-newest 报错，代理错误，vedio就没有代理错误为什么
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
		}
		return getUsersConnectionRepository(connectionFactoryLocator).createConnectionRepository(authentication.getName());
	}

	/**
	 * social登陆的配置类
	 * <p>
	 * 里面new了一个SocialAuthenticationFilter(社交登陆相关的过滤器)，加入Filter链中，
	 **/
	@Bean
	public SpringSocialConfigurer imoocSocialSecurityConfig() {
		String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
		//把自己的写的子类作为配置类，把/auth路径做成可配置
		ImoocSpringSocialConfigurer configurer = new ImoocSpringSocialConfigurer(filterProcessesUrl);
		//设置跳转回来的注册页面
		configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
		//设置后处理器
		configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
		return configurer;
	}

	/**
	 * 解决两个问题
	 * <p>
	 * 1. 注册页面如何拿到原来的社交账号的信息
	 * <p>
	 * 2. 注册完成后如何把用户ID传回给原来的springSocial的，供{@link JdbcUsersConnectionRepository}使用
	 */
	@Bean
	public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator,
	                                               UsersConnectionRepository usersConnectionRepository) {
		return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
		//与直接调用bean方法是同一个对象
		//UsersConnectionRepository usersConnectionRepository1 = getUsersConnectionRepository(connectionFactoryLocator);
		//return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository1);
	}
}
