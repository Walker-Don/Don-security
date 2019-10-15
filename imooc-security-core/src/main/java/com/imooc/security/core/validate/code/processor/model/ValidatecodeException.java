package com.imooc.security.core.validate.code.processor.model;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName ValidatecodeException
 * @date 2019年10月11日 下午 5:51
 */
public class ValidatecodeException extends AuthenticationException {

    public ValidatecodeException(String msg)  {
        super(msg);
    }

}