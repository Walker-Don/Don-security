package com.imooc.security.core.validate.code;

import com.imooc.security.core.validate.code.codeGenerator.ImageCodeGenerator;
import com.imooc.security.core.validate.code.codeGenerator.SmsCodeGenerator;
import com.imooc.security.core.validate.code.smsSender.DefaultSmsCodeSender;
import com.imooc.security.core.validate.code.smsSender.SmsCodeSender;
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

    //工厂方法配置可以使用@ConditionalOnMissingBean
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")//有bean就不使用这个
    public ImageCodeGenerator defaultImageCodeGenerator() {
        ImageCodeGenerator ImageCodeGenerator = new ImageCodeGenerator();
        //有了Bean工厂方法不需要主动设置securityProperties
        //ImageCodeGenerator.setSecurityProperties(securityProperties);
        return ImageCodeGenerator;
    }

    @Bean
    @ConditionalOnMissingBean(name = "smsCodeGenerator")//有bean就不使用这个
    public SmsCodeGenerator defaultSmsCodeGenerator() {
        return new SmsCodeGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)//有bean就不使用这个
    public SmsCodeSender defaultSmsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}
