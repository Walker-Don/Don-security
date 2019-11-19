/**
 *
 */
package com.imooc.security.core.social.qq.connnect;

import com.imooc.security.core.social.qq.api.QQApi;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * 在config中配置，用于生成Connection
 * <p>
 * 每个社交网站都不一样
 * <p>
 * 每个ConnectionFactory 和 特定providerId 对应，验证时在serviceLocator中找出来，并且有自己特有的ServiceProvider和Adapter
 *
 * @author zhailiang
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQApi> {

	public QQConnectionFactory(String providerId, String appId, String appSecret) {
		super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
	}

}
