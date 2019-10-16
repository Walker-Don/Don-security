package com.imooc.security.core.validate.code.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * todo
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月12日 下午 10:50
 */
public class DefaultSmsCodeSender implements SmsCodeSender {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void send(String mobile, String code) {
        logger.warn("假装发送sms短信["+code+"]到特定手机:"+mobile);
    }
}
