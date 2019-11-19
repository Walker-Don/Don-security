package com.imooc.security.core;

import com.imooc.security.core.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 启动自动读取配置
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName SecurityCoreConfig
 * @date 2019年10月10日 下午 3:53
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)//快速加入容器。等价于@Bean
public class SecurityCoreConfig {
}
