package com.imooc.security.core.validate.code.codeGenerator;

import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.model.ValidateCode;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * todo
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月12日 下午 8:16
 */

public class SmsCodeGenerator implements ValidateCodeGenerator {
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 生成给定长度、过期时间的validateCode
     * @param servletWebRequest
     * @return
     */
    @Override
    public ValidateCode createCode(ServletWebRequest servletWebRequest) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }
}
