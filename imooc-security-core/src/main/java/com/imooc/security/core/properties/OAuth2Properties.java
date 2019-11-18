package com.imooc.security.core.properties;

import lombok.Data;

/**
 * 配置OAuth2的属性，现在只有clients
 */
@Data
public class OAuth2Properties {

	private OAuth2ClientProperties[] clients = {};

}
