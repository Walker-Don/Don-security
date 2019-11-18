package com.imooc.security.app;

import com.imooc.security.core.properties.OAuth2ClientProperties;
import com.imooc.security.core.properties.SecurityProperties;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 认证服务器配置
 * Convenient strategy for configuring an OAUth2 Authorization Server. Beans of this type are applied to the Spring context automatically if you @EnableAuthorizationServer.
 */
@Configuration
@EnableAuthorizationServer
public class ImoocAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenStore tokenStore;

	/**
	 * endpoints是配置申请令牌的入口点，
	 * 继承AuthorizationServerConfigurerAdapter后这两个bean都要自己配置，因此需要引进来
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore)//存储令牌
				.authenticationManager(authenticationManager)
				.userDetailsService(userDetailsService);
	}

	/**
	 * 客户端相关应用，那些client可以申请令牌
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//只开放给自己的client使用，不会开放给外部的的人注册client，所以不需要jdbc
		InMemoryClientDetailsServiceBuilder builder = clients.inMemory();

		//读取配置文件中的client
		if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
			for (OAuth2ClientProperties client : securityProperties.getOauth2().getClients()) {
				builder.withClient(client.getClientId())//clientId
						.secret(client.getClientSecret())//clientSecret
						.accessTokenValiditySeconds(client.getAccessTokenValidateSeconds())//有效时间
						.authorizedGrantTypes("refresh_token", "password")//申请方式
						.scopes("all", "read", "write");//不带参数可以，要带必须范围内
			}
		}

		/*clients.inMemory()
				.withClient("test")
				.secret("test")
				.accessTokenValiditySeconds(600)
				.and()
				.withClient("imooc")//和yml效果一样
				.secret("imoocsecret")//和yml效果一样
				.accessTokenValiditySeconds(1200)//令牌有效时间
				.authorizedGrantTypes("refresh_token", "password")//申请方式
				.scopes("all", "read", "write");//不带参数可以，要带必须范围内*/

	}
}
