/**
 *
 */
package com.imooc.security.app;

import com.imooc.security.core.social.ImoocSpringSocialConfigurer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 所有的bean初始化前后都会经过这个类，
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	//如果是imoocSocialSecurityConfig这个bean，就给他更改signUp，模块级别的修改
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (StringUtils.equals(beanName, "imoocSocialSecurityConfig")) {
			ImoocSpringSocialConfigurer config = (ImoocSpringSocialConfigurer) bean;
			config.signupUrl("/social/signUp");
			return config;
		}
		return bean;
	}

}
