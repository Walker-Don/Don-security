package com.imooc.security.core.validate.code;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 保存ValidateCodeProcessor，免得多次注入依赖
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月15日 下午 8:52
 */
@Component
@Getter
public class ValidateCodeProcessorHolder {
	@Autowired
	private Map<String, ValidateCodeProcessor> validateCodeProcessors;

	/**
	 * 使用ValidateCodeType来定位特定的processor
	 *
	 * @param type
	 * @return
	 */
	public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
		return findValidateCodeProcessor(type.toString().toLowerCase());
	}

	/**
	 * 使用String来定位
	 * <p>
	 * 例如sms,image
	 *
	 * @param type
	 * @return
	 */
	public ValidateCodeProcessor findValidateCodeProcessor(String type) {
		//不用字符串是为了改类名字的时候便于维护
		String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
		ValidateCodeProcessor processor = validateCodeProcessors.get(name);
		if (processor == null) {
			throw new ValidateCodeException("验证码处理器" + name + "不存在");
		}
		return processor;
	}
}
