package com.imooc.security.core.validate.code.processor.generator;

import com.imooc.security.core.validate.code.processor.model.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 生成各种ValidatorCode的根接口
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月12日 上午 11:19
 */
public interface ValidateCodeGenerator {
    /**
     * 生成ValidateCode的接口方法
     * @param servletWebRequest
     * @return
     */
    ValidateCode generateCode(ServletWebRequest servletWebRequest);

}
