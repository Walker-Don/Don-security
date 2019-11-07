/**
 *
 */
package com.imooc.security.core.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * @author zhailiang
 */
@Data
//继承自spring框架的SocialProperties 有appId和appSecret
public class QQProperties extends SocialProperties {

	private String providerId = "qq";//设置providerId  "/auth/qq"中的qq

}
