package com.imooc.code;

import com.imooc.security.core.validate.code.ImageCode;
import com.imooc.security.core.validate.code.ValidateCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 自己实现的ValidateCodeGenerator
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月12日 下午 12:04
 */
@Component("validateCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ImageCode createImageCode(ServletWebRequest servletWebRequest) {
        logger.warn("更高级的图形验证码生成代码");
        return null;
    }
}
