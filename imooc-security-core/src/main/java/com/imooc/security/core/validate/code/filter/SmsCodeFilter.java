package com.imooc.security.core.validate.code.filter;

import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.processor.model.ValidatecodeException;
import com.imooc.security.core.validate.code.processor.model.ValidateCode;
import com.imooc.security.core.validate.code.processor.SmsCodeProcessor;
import lombok.Data;
import org.apache.commons.lang.ArrayUtils;
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
 * 只针对特定url
 * 这个类不自动@Autowired
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName ValidateCodeFilter
 * @date 2019年10月11日 下午 7:15
 */
@Data
//实现接口InitializingBean
public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private AuthenticationFailureHandler authenticationFailureHandler;

    //后面自己设置，个性化
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    //后面自己设置，个性化
    private SecurityProperties securityProperties;

    //存储需要启用doFilter的特定url
    private Set<String> urls = new HashSet<>();

    //
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    //初始化urls，把配置文件中的url读出来放在urls中
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getSms().getUrl(), ",");
        if (ArrayUtils.isNotEmpty(configUrls)) {
            urls.addAll(Arrays.asList(configUrls));

        }
        urls.add("/authentication/mobile");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean action = false;

        //判断过滤原则，符合就开启过滤流程，不中就下一个filter
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
        //从session中取出ValidateCode
        ValidateCode smsCodeInSession = (ValidateCode) sessionStrategy.getAttribute(servletWebRequest, SmsCodeProcessor.SESSION_KEY_FOR_CODE_SMS);
        //取出后应该马上移除出session  todo 从数据库中移除？
        sessionStrategy.removeAttribute(servletWebRequest, SmsCodeProcessor.SESSION_KEY_FOR_CODE_SMS);
        //request中传上来的参数imageCode
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "smsCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidatecodeException("验证码的值不能为空");
        }

        if (smsCodeInSession == null) {
            throw new ValidatecodeException("验证码不存在");
        }

        if (smsCodeInSession.isExpired()) {
            sessionStrategy.removeAttribute(servletWebRequest, SmsCodeProcessor.SESSION_KEY_FOR_CODE_SMS);
            throw new ValidatecodeException("验证码已过期");
        }

        if (!StringUtils.equals(smsCodeInSession.getCode(), codeInRequest)) {
            throw new ValidatecodeException("验证码不匹配");
        }
    }
}
