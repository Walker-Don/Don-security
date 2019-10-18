package com.imooc.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
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
public class MyUserDetailsService implements UserDetailsService, SocialUserDetailsService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.warn("登录名" + username);
        //加密密码，因为密码放进数据库前都是先加密，因此拿出来的时候是encodedPassword
        return buildUser(username);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        logger.info("社交登录用户Id:" + userId);
        return buildUser(userId);
    }

    private SocialUserDetails buildUser(String userId) {
        String encodedPassword = passwordEncoder.encode("1234");
        logger.warn("登录密码" + encodedPassword);

        //UserDetails接口的数个检验逻辑，其中一个是false都不能成功认证
        //业务中用自己的pojo类实现或者继承
        return new SocialUser(userId, encodedPassword,
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
