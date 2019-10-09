package com.imooc.security.browser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 实现UserDetailsService可以自定义用户认证逻辑
 * <p>
 * 原理是：获取用户名，在这里封装用户名相关的密码等信息（如何封装属于业务逻辑），然后与http发来的相比较，判断是否认证成功
 * <p>
 * 登陆的时候这个方法只调用一次，后续不需要再次调用，
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName MyUserDetailsService
 * @date 2019年10月09日 下午 8:06
 */
@Component
public class MyUserDetailsService implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.warn("登录名" + username);
        return new User(username, "1234", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
