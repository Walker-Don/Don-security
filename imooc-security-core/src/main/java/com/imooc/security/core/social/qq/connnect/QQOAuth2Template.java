package com.imooc.security.core.social.qq.connnect;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * 重写OAuth2Template，发送HTTP请求，解析返回信息，封装accessToken
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月18日 下午 9:27
 */
public class QQOAuth2Template extends OAuth2Template {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
		super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
		setUseParametersForClientAuthentication(true);//带上ClientId和clientSecret
	}

	//自己发送，自己封装，返回的信息不是json格式
	@Override
	protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
		// 1.发送http请求，接受的格式是String
		String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);

		logger.warn("获取accessToke的响应：" + responseStr);
		// 2.解析模板：“access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14”
		String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr, "&");

		String accessToken = StringUtils.substringAfterLast(items[0], "=");
		Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
		String refreshToken = StringUtils.substringAfterLast(items[2], "=");
		// 3.封装到AccessGrant
		return new AccessGrant(accessToken, null, refreshToken, expiresIn);
	}

	//添加处理http请求返回信息的converter
	@Override
	protected RestTemplate createRestTemplate() {
		RestTemplate restTemplate = super.createRestTemplate();
		//可以处理ContentType：html
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return restTemplate;
	}
}
