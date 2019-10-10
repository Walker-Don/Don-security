package com.imooc.security.browser;

import com.imooc.security.browser.model.SimpleResponse;
import com.imooc.security.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 需要认证时跳转到这里来
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName BrowserSecurityController
 * @date 2019年10月10日 下午 3:17
 */
@RestController
public class BrowserSecurityController {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    //把请求缓存起来,怎么起作用的？
    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    @RequestMapping("/authentication/require")
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
}
