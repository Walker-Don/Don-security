package com.imooc.security.core.social.qq.connnect;

import com.imooc.security.core.social.qq.api.DefaultQQApi;
import com.imooc.security.core.social.qq.api.QQApi;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * QQSocial相关的serviceProvider，两部分
 * <p>
 * OAuth2Template用来获取accessToken，
 * <p>
 * getApi()生成providerId对应的Api
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月18日 下午 12:20
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQApi> {

	private String appId;//不需要appSecret

	private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

	private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

	/**
	 * Create a new {@link AbstractOAuth2ServiceProvider}.
	 */
	public QQServiceProvider(String appId, String appSecret) {
		super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
		this.appId = appId;
	}

	@Override
	public QQApi getApi(String accessToken) {
		return new DefaultQQApi(accessToken, appId);
	}
}
