package com.imooc.security.core.properties;

import lombok.Data;

/**
 * 每个client的属性
 */
@Data
public class OAuth2ClientProperties {

	private String clientId;

	private String clientSecret;

	private int accessTokenValidateSeconds = 7200;//0代表无限时间

}
