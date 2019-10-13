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
public class ImageCodeProperties  extends  SmsCodeProperties{

    private int width = 67; //宽
    private int height = 23;//长

    /**
         * 需要拦截的url，需要使用图形验证码验证
     */
    public ImageCodeProperties() {
        super.setLength(4);
    }

}
