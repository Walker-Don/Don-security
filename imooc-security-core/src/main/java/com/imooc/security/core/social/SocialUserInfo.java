/**
 *
 */
package com.imooc.security.core.social;

import lombok.Data;

/**
 * @author zhailiang
 */
@Data
public class SocialUserInfo {

	private String providerId;

	/**
	 * 也就是openID
	 */
	private String providerUserId;

	private String nickname;

	private String headimg;

}
