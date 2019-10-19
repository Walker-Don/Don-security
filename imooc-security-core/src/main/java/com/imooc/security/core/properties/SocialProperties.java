/**
 * 
 */
package com.imooc.security.core.properties;

import lombok.Data;

/**
 * @author zhailiang
 *
 */
@Data
public class SocialProperties {

	private QQProperties qq = new QQProperties();

	private String filterProcessesUrl = "/auth";//配置social拦截的  "/auth/social"
}
