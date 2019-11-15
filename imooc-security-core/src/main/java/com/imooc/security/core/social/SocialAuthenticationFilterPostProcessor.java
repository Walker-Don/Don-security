package com.imooc.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * social授权后的后处理器，可以添加自己的后处理器，比如app就可以申请令牌的后处理器，
 * 其实就是拿到socialAuthenticationFilter进行设置，这样太麻烦了 todo 可以变更，直接设置filter的successHandler
 * @author zhailiang
 *
 */
public interface SocialAuthenticationFilterPostProcessor {
	
	void process(SocialAuthenticationFilter socialAuthenticationFilter);

}
