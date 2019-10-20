package com.imooc.security.browser;

import com.imooc.security.browser.model.SimpleResponse;
import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.social.SocialUserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登陆前访问受保护资源需要认证，这时跳转到这里来
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName BrowserSecurityController
 * @date 2019年10月10日 下午 3:17
 */
@RestController
public class BrowserSecurityController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //把请求缓存起来,怎么起作用的？
    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public SimpleResponse require(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //先缓存
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {

            String targetUrl = savedRequest.getRedirectUrl();
            logger.warn("引发跳转的请求是:" + targetUrl);

            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                //跳转到登陆认证界面
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }
        }
        //若是重定向，那么return就被丢弃
        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");

    }

    /**
     * 在注册页上发这个请求就可以拿到用户的connection信息
     * @param request
     * @return
     */
    @GetMapping("/social/user")
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
