package com.imooc.security.core.properties;

import lombok.Data;

/**
 * SecurityProperties的field，内包括相关验证码参数
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName ValidateCodeProperties
 * @date 2019年10月11日 下午 8:30
 */
@Data
public class ValidateCodeProperties {
	private ImageCodeProperties image = new ImageCodeProperties();
	private SmsCodeProperties sms = new SmsCodeProperties();

}
