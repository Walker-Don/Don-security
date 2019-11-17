/**
 *
 */
package com.imooc.security.app.authentication.oponid;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhailiang
 */
public class OpenIdAuthenticationProvider implements AuthenticationProvider {

	@Getter
	@Setter
	private SocialUserDetailsService userDetailsService;

	@Getter
	@Setter
	private UsersConnectionRepository usersConnectionRepository;

	/**
	 * 对传过来的 OpenIdAuthenticationToken 进行检验
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		//1. 转化OpenIdAuthenticationToken
		OpenIdAuthenticationToken authenticationToken = (OpenIdAuthenticationToken) authentication;

		//2. 去UsersConnectionRepository查找connection表，找对对应的userId（openId认证方法特别之处）
		Set<String> providerUserIds = new HashSet<>();
		providerUserIds.add((String) authenticationToken.getPrincipal());
		Set<String> userIds = usersConnectionRepository.findUserIdsConnectedTo(authenticationToken.getProviderId(), providerUserIds);

		if (CollectionUtils.isEmpty(userIds) || userIds.size() != 1) {
			throw new InternalAuthenticationServiceException("无法获取用户信息");
		}

		String userId = userIds.iterator().next();

		//3. 查找user是否存在
		UserDetails user = userDetailsService.loadUserByUserId(userId);

		if (user == null) {
			throw new InternalAuthenticationServiceException("无法获取用户信息");
		}

		//4. 用户存在，返回authenticated Authentication
		OpenIdAuthenticationToken authenticationResult = new OpenIdAuthenticationToken(user, user.getAuthorities());

		authenticationResult.setDetails(authenticationToken.getDetails());

		return authenticationResult;
	}

	/**
	 * 用于判断authRequest是否适用这个provider
	 *
	 * @param authentication
	 * @return
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return OpenIdAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
