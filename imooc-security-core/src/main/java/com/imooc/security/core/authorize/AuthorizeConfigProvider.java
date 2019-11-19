package com.imooc.security.core.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 授权配置的提供者接口，把http的安全配置的代码剥离出去，然后在核心模块中依次收集并引用
 * <p>
 * 默认实现类 {@link ImoocAuthorizeConfigProvider}
 * <p>
 * 新的模块如果需要对url进行配置可以实现自己的实现类
 *
 * @author zhailiang
 */
public interface AuthorizeConfigProvider {

	/**
	 * 配置url的授权策略
	 *
	 * @param config http.authorizeRequests()的返回
	 */
	void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}
