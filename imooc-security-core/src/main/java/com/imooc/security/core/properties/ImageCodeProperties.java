package com.imooc.security.core.properties;

import lombok.Data;

/**
 * 图形验证码默认配置
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName ImageCodeProperties
 * @date 2019年10月11日 下午 8:32
 */
@Data
public class ImageCodeProperties {
    private int width = 67;
    private int height = 23;
    private int length = 4;
    private int expireIn = 60;

}
