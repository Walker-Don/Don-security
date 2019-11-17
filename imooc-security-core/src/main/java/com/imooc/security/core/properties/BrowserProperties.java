package com.imooc.security.core.properties;

import lombok.Data;

/**
 * 与Browser相关的配置类
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName BrowserProperties
 * @date 2019年10月10日 下午 3:56
 */
@Data
public class BrowserProperties {
	/**
	 * 默认的登陆页面
	 */
	private String loginPage = SecurityConstants.DEFAULT_LOGIN_PAGE_URL;
	private String logoutSuccessPage = "-"; //不能赋予空值或者null
	private LoginResponseType loginResponseType = LoginResponseType.JSON;

	/**
	 * 默认1h，记住我功能
	 */
	private int rememberMeSeconds = 3600;
	/**
	 * 是否保持记住我功能，默认false
	 */
	private boolean alwaysRemember = false;

	private String passedUrls;
	private String signUpUrl = "/imooc-signUp.html";

	private SessionProperties session = new SessionProperties();

}
