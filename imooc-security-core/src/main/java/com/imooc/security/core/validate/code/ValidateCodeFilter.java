package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityProperties;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 处理验证码的逻辑，放在filter中处理
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName ValidateCodeFilter
 * @date 2019年10月11日 下午 7:15
 */
@Data
//实现接口InitializingBean
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private AuthenticationFailureHandler authenticationFailureHandler;

    //后面自己设置，个性化
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    //后面自己设置，个性化
    private SecurityProperties securityProperties;

    private Set<String> urls = new HashSet<>();

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        urls.addAll(Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getImage().getUrl(), ",")));
        urls.add("/authentication/form");

    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean action = false;

        //判断过滤原则
        for (String url : urls) {
            if (antPathMatcher.match(url, request.getRequestURI())) {
                action = true;
                break;
            }
        }

        if (action
            //                && StringUtils.equalsIgnoreCase("post", request.getMethod())
                ) {
            try {
                //取出请求，验证请求中的图形码是否和session中的正确
                validate(new ServletWebRequest(request));
            } catch (ValidatecodeException e) {

                //认证失败，直接调用失败控制器
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                //直接返回，不要再走下面过滤器
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    //图片验证码的逻辑
    private void validate(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
        //从session中取出ImageCode
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
        //取出后应该马上移除出session  todo 从数据库中移除？
        sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
        //request中传上来的参数imageCode
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidatecodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidatecodeException("验证码不存在");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
            throw new ValidatecodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidatecodeException("验证码不匹配");
        }

    }
}
