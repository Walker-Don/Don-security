/**
 *
 */
package com.imooc.security.core.social;

import lombok.Data;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 改良现在的SpringSocialConfigurer，替换成自己用的回调地址
 *
 * @author zhailiang
 */
@Data
public class ImoocSpringSocialConfigurer extends SpringSocialConfigurer {

	//做成可配置的，放在security中,在构造函数中传进来，在方法中设置
	private String filterProcessesUrl;

	private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

	public ImoocSpringSocialConfigurer(String filterProcessesUrl) {
		this.filterProcessesUrl = filterProcessesUrl;
	}

	//设置我们自己的回调地址
	//object就是我们需要放到过滤器链上的那个Filter，这个方法就是获取filter
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T postProcess(T object) {
		SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
		filter.setFilterProcessesUrl(filterProcessesUrl);
		//如果项目没有实现自己的后处理器，那么就没有任何行为
		if (socialAuthenticationFilterPostProcessor != null) {
			socialAuthenticationFilterPostProcessor.process(filter);
		}
		return (T) filter;
	}

}
