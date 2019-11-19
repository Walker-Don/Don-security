package com.imooc.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName ValidateCodeException
 * @date 2019年10月11日 下午 5:51
 */
public class ValidateCodeException extends AuthenticationException {

	public ValidateCodeException(String msg) {
		super(msg);
	}

}