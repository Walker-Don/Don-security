package com.imooc.security.browser;

import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.filter.SmsCodeFilter;
import com.imooc.security.core.validate.code.filter.ValidateCodeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * BrowserSecurityConfig配置类，
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName BrowserSecurityConfig
 * @date 2019年10月09日 上午 11:32
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    //PersistentTokenRepository 记住我功能
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    //PasswordEncoder
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //需要预先认证的页面会调用security配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        logger.warn("启动HttpSecurity配置");

        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet();

        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
        smsCodeFilter.setSecurityProperties(securityProperties);
        smsCodeFilter.afterPropertiesSet();

        //super.configure(http);
        http
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)//image认证码过滤器
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)//sms认证码过滤器

                .formLogin()//需要表单登陆,认证
                //http.httpBasic()//httpbasic登陆
                .loginPage("/authentication/require")//当需要认证时跳转到这个地址，在这里判断请求是restful还是html
                .loginProcessingUrl("/authentication/form")//表单提交的地址，自定义，会过滤
                .successHandler(imoocAuthenticationSuccessHandler)//登陆成功操作
                .failureHandler(imoocAuthenticationFailureHandler)//登陆失败操作

                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository)  // .tokenRepository(persistentTokenRepository())视频重新new一个，何必bean‘
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)

                .and()
                .authorizeRequests()//需要请求授权
                .antMatchers("/authentication/require",
                        "/code/*", "/index.html", securityProperties.getBrowser().getLoginPage())
                .permitAll()//跳过，不需要登陆，能够通过springSecurity自定义的filter，但是不一定能够通过自己定义的filter
                .anyRequest().authenticated()//任何请求都需要身份认证

                .and()
                .csrf().disable()//关闭csrf

                .apply(smsCodeAuthenticationSecurityConfig);
    }
}
