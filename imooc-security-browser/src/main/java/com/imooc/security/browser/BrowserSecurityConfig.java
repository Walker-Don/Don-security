package com.imooc.security.browser;

import com.imooc.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    //需要预先认证的页面会调用security配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("启动HttpSecurity配置");
        //super.configure(http);
        http.formLogin()//需要表单登陆,认证
                //http.httpBasic()//httpbasic登陆
                .loginPage("/authentication/require")//当需要认证时跳转到这个地址，在这里判断请求是restful还是html
                .loginProcessingUrl("/authentication/form")//表单提交的地址，自定义
                .successHandler(imoocAuthenticationSuccessHandler)//登陆成功操作
                .failureHandler(imoocAuthenticationFailureHandler)//登陆失败操作
                .and()
                .authorizeRequests()//需要请求授权
                .antMatchers("/authentication/require", "/test.html", securityProperties.getBrowser().getLoginPage()).permitAll()//跳过
                .anyRequest().authenticated()//任何请求都需要身份认证
                .and()
                .csrf().disable();//关闭csrf
    }
}
