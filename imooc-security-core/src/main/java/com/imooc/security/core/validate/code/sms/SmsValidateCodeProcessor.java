package com.imooc.security.core.validate.code.sms;

import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.validate.code.AbstractValidateCodeProcessor;
import com.imooc.security.core.validate.code.ValidateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * todo
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月13日 下午 4:52
 */
@Component
public class SmsValidateCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    @Autowired
    private SmsCodeSender smsCodeSender;

    @Override
    protected void send(ServletWebRequest servletWebRequest, ValidateCode validateCode) throws Exception {
        String mobile = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(),
                SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE);
        smsCodeSender.send(mobile, validateCode.getCode());
    }
}
