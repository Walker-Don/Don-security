package com.imooc.security.core.validate.code.config;

import com.imooc.security.core.validate.code.processor.generator.ImageCodeGenerator;
import com.imooc.security.core.validate.code.processor.generator.SmsCodeGenerator;
import com.imooc.security.core.validate.code.processor.smsSender.DefaultSmsCodeSender;
import com.imooc.security.core.validate.code.processor.smsSender.SmsCodeSender;
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
    @Bean //注意bean的名字方法名
    @ConditionalOnMissingBean(name = "imageCodeGenerator")//有bean就不使用这个
    public ImageCodeGenerator imageCodeGenerator() {
        ImageCodeGenerator ImageCodeGenerator = new ImageCodeGenerator();
        //有了Bean工厂方法不需要主动设置securityProperties
        //ImageCodeGenerator.setSecurityProperties(securityProperties);
        return ImageCodeGenerator;
    }

    @Bean
    @ConditionalOnMissingBean(name = "smsCodeGenerator")//有bean就不使用这个
    public SmsCodeGenerator smsCodeGenerator() {
        return new SmsCodeGenerator();
    }

    @Bean("smsCodeSender")
    @ConditionalOnMissingBean(SmsCodeSender.class)//有bean就不使用这个
    public SmsCodeSender defaultSmsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}
