package com.imooc.security.core.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 关于social登陆的controller，可以在social注册时获取个人信息
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月24日 下午 2:26
 */
@RestController()
@RequestMapping("/social")
//@DependsOn("socialConfig")
public class SocialController {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    /**
     * 在注册页上发这个请求就可以拿到用户的connection信息
     *
     * @param request
     * @return
     */
    @GetMapping("/user")
    public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
        SocialUserInfo socialUserInfo = null;
        //在跳转signUp页面的时候把Connection放进了session
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        if (connection != null) {
            socialUserInfo = new SocialUserInfo();
            socialUserInfo.setProviderId(connection.getKey().getProviderId());
            socialUserInfo.setProviderUserId(connection.getKey().getProviderUserId());
            socialUserInfo.setNickname(connection.getDisplayName());
            socialUserInfo.setHeadimg(connection.getImageUrl());
        }
        return socialUserInfo;
    }
}
