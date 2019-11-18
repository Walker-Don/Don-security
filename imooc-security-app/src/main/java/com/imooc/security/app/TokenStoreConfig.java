package com.imooc.security.app;

import com.imooc.security.app.jwt.ImoocJwtTokenEnhancer;
import com.imooc.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 存储令牌，避免关闭应用后不能使用令牌
 * 令牌频繁存储，使用redis进行token的存储，redis的性能要快很多
 */
@Configuration
public class TokenStoreConfig {

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;//redis的连接工厂

	//存储令牌
	@Bean
	@ConditionalOnProperty(prefix = "imooc.security.oauth2", name = "tokenStore", havingValue = "redis")//必须配redis才生效，jwt是默认
	public TokenStore tokenStore() {
		return new RedisTokenStore(redisConnectionFactory);
	}

	//jwt 的相关config
	@Configuration
	@ConditionalOnProperty(prefix = "imooc.security.oauth2", name = "tokenStore", havingValue = "jwt", matchIfMissing = true)
//focus 不配置该配置项也是生效的，如果配置了需要match才可以生效，默认使用jwt令牌
	public static class JwtConfig {

		@Autowired
		private SecurityProperties securityProperties;

		//存储jwt令牌
		@Bean
		public TokenStore jwtTokenStore() {
			return new JwtTokenStore(jwtAccessTokenConverter());
		}

		//加密签，该bean是TokenEnhancer的子类
		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter() {
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			converter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());//增加密钥，用来签名，默认utf-8
			return converter;
		}

		//对accessToken的增强器，扩展信息，
		@Bean
		@ConditionalOnBean(TokenEnhancer.class)//该bean是TokenEnhancer的子类，因此需要配合@ConditionalOnBean
		public TokenEnhancer jwtTokenEnhancer() {
			return new ImoocJwtTokenEnhancer();
		}

	}

}
