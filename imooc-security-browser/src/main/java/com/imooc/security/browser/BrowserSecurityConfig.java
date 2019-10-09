package com.imooc.security.browser;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * todo
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName BrowserSecurityConfig
 * @date 2019年10月09日 上午 11:32
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("启动HttpSecurity配置");
        //super.configure(http);
        //http.formLogin()//需要表单登陆,认证
        http.httpBasic()//httpbasic登陆
                .and()
                .authorizeRequests()//需要请求授权
                .anyRequest()//任何请求
                .authenticated();//都需要身份认证
    }
}
