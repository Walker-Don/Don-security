package com.imooc.security.core.validate.code.processor;

import com.imooc.security.core.validate.code.model.ValidateCode;
import com.imooc.security.core.validate.code.smsSender.SmsCodeSender;
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
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    public static final String SESSION_KEY_FOR_CODE_SMS = "SESSION_KEY_FOR_CODE_SMS";

    @Autowired
    private SmsCodeSender smsCodeSender;

    @Override
    protected void send(ServletWebRequest servletWebRequest, ValidateCode validateCode) throws Exception {
        String mobile = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(), "mobile");
        smsCodeSender.send(mobile, validateCode.getCode());
    }
}
