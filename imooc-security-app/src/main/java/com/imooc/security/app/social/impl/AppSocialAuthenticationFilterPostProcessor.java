/**
 *
 */
package com.imooc.security.app.social.impl;

import com.imooc.security.core.social.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author zhailiang
 */
@Component
public class AppSocialAuthenticationFilterPostProcessor implements SocialAuthenticationFilterPostProcessor {

	@Autowired
	private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

	@Override
	public void process(SocialAuthenticationFilter socialAuthenticationFilter) {
		socialAuthenticationFilter.setAuthenticationSuccessHandler(imoocAuthenticationSuccessHandler);
	}

}
