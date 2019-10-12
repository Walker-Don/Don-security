package com.imooc.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 生成各种ValidatorCode的接口
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月12日 上午 11:19
 */
public interface ValidateCodeGenerator {
    /**
     * 生成ImageCode的接口方法
     * @param servletWebRequest
     * @return
     */
    ImageCode createImageCode(ServletWebRequest servletWebRequest);

}
