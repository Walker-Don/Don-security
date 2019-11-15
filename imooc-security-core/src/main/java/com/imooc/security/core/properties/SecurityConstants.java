package com.imooc.security.core.properties;

import com.imooc.security.core.validate.code.ValidateCodeController;

/**
 * 定义SecurityCore框架所需要用到的constants
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月15日 下午 7:40
 */
public class SecurityConstants {

	/**
	 * 默认的处理验证码的url前缀
	 */
	public static final String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";

	/**
	 * 当请求需要身份认证时，默认跳转的url
	 *
	 * @see ValidateCodeController
	 */
	public static final String DEFAULT_UNAUTHENTICATION_URL = "/authentication/require";
	/**
	 * 默认的用户名密码登录请求处理url
	 */
	public static final String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/authentication/form";
	/**
	 * 默认的手机验证码登录请求处理url
	 */
	public static final String DEFAULT_LOGIN_PROCESSING_URL_MOBILE = "/authentication/mobile";
	/**
	 * 默认登录页面
	 */
	public static final String DEFAULT_LOGIN_PAGE_URL = "/imooc-signIn.html";
	/**
	 * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
	 */
	public static final String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";
	/**
	 * session失效的跳转地址
	 */
	public static final String DEFAULT_SESSION_INVALID_URL = "/imooc-session-invalid.html";

	/**
	 * openid参数名
	 */
	public static final String DEFAULT_PARAMETER_NAME_OPENID = "openId";
	/**
	 * providerId参数名
	 */
	public static final String DEFAULT_PARAMETER_NAME_PROVIDERID = "providerId";

	/**
	 * 默认的OPENID登录请求处理url
	 */
	public static final String DEFAULT_LOGIN_PROCESSING_URL_OPENID = "/authentication/openid";
}
