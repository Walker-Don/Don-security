package com.imooc.security.core.properties;

import lombok.Data;

/**
 * sms属性
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月12日 下午 10:31
 */
@Data
public class SmsCodeProperties {
    private int length = 6;
    private int expireIn = 60; //过期时间
    private String url;
}
