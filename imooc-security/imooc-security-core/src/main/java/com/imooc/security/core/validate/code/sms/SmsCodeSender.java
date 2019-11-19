package com.imooc.security.core.validate.code.sms;

/**
 * 给手机发送验证码
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月12日 下午 10:49
 */
public interface SmsCodeSender {

	void send(String mobile, String code);
}
