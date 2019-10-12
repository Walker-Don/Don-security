package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * todo
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月12日 上午 11:20
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    //工厂方法配置可以使用@ConditionalOnMissingBean
    @Bean
    @ConditionalOnMissingBean(name = "validateCodeGenerator")//有bean就不使用这个
    public ValidateCodeGenerator imageCodeGenerator() {
        DefaultValidateCodeGenerator DefaultValidateCodeGenerator = new DefaultValidateCodeGenerator();
        DefaultValidateCodeGenerator.setSecurityProperties(securityProperties);
        return DefaultValidateCodeGenerator;
    }

}
