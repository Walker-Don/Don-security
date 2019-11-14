package com.imooc.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 管理生成、保存、发送的
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月13日 下午 1:52
 */
public interface ValidateCodeProcessor {

	/**
	 * 创建校验码
	 *
	 * @param servletWebRequest
	 * @throws Exception
	 */
	void create(ServletWebRequest servletWebRequest) throws Exception;

	/**
	 * 校验验证码
	 */

	void validate(ServletWebRequest servletWebRequest);
}
