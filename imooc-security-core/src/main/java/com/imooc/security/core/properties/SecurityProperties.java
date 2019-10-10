package com.imooc.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * security配置类，包括其他配置类
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName SecurityProperties
 * @date 2019年10月10日 下午 3:55
 */
@Data
@ConfigurationProperties("imooc.security")
public class SecurityProperties {
    private BrowserProperties browser = new BrowserProperties();
}
