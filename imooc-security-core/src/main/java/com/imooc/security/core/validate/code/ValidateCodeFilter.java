package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import lombok.Data;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 处理验证码的逻辑，放在filter中处理
 * 只针对特定url
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName ValidateCodeFilter
 * @date 2019年10月11日 下午 7:15
 */
@Data
//实现接口InitializingBean
@Component
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired //预防没有
	private AuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();

	@Autowired
	private ValidateCodeProcessorHolder validateCodeProcessorHolder;

	//后面自己设置，个性化
	@Autowired
	private SecurityProperties securityProperties;

	//存储需要启用doFilter的特定url-ValidateCodeType键值对
	private Map<String, ValidateCodeType> urlsMap = new HashMap<>();

	/**
	 * 验证请求url与配置的url是否匹配的工具类 Todo和StringUtils用法不一样？
	 */
	private AntPathMatcher pathMatcher = new AntPathMatcher();

	//初始化urls，把配置文件中的url读出来放在urlsMap中
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();

		//需要Image的urls,DEFAULT_LOGIN_PROCESSING_URL_FORM
		urlsMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
		addConfigureUrls(securityProperties.getCode().getImage().getUrl(), ValidateCodeType.IMAGE);

		//需要sms的urls，默认DEFAULT_LOGIN_PROCESSING_URL_MOBILE
		urlsMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
		addConfigureUrls(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);
	}

	/**
	 * 检验的规则，request与map对比有没有符合的url
	 * 特点是用ValidateCodeType来定位特定的类
	 * 在这里已经根据url字符串比较来find实际需要使用的实现类，后面调用方法可以不用再找，直接调用getClass就可以找到，运用多态
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		//获取uri代表的ValidateCodeType
		ValidateCodeType validateCodeType = getValidateType(request);

		if (validateCodeType != null) {
			try {
				//取出请求，验证请求中的图形码是否和session中的正确
				validateCodeProcessorHolder.findValidateCodeProcessor(validateCodeType).validate(new ServletWebRequest(request));
				logger.warn("验证通过");
			} catch (ValidateCodeException e) {
				//认证失败，直接调用失败控制器
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
				//直接返回，不要再走下面过滤器
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	//添加配置文件中的url-ValidateCodeType键值对到urlsMap中
	private void addConfigureUrls(String urls, ValidateCodeType image) {
		if (StringUtils.isNotBlank(urls)) {
			String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urls, ",");
			if (ArrayUtils.isNotEmpty(configUrls)) {
				for (String url : configUrls) {
					urlsMap.put(url, image);
				}
			}
		}
	}

	/**
	 * 获取校验码的类型，如果当前请求不需要校验，则返回null
	 *
	 * @param request
	 * @return
	 */
	private ValidateCodeType getValidateType(HttpServletRequest request) {
		//待检查uri
		String requestURI = request.getRequestURI();

		ValidateCodeType result = null;

		//需要非get的请求
		if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {

			Set<Map.Entry<String, ValidateCodeType>> urlTypeEntries = urlsMap.entrySet();

			//判断过滤原则，符合就开启过滤流程，不中就下一个filter
			for (Map.Entry<String, ValidateCodeType> entry : urlTypeEntries) {

				//上下两个方法的区别
				//if (StringUtils.equalsIgnoreCase(entry.getKey(), requestURI)) {
				if (pathMatcher.match(entry.getKey(), requestURI)) {

					result = entry.getValue();
					logger.warn("检验请求url： " + request.getRequestURL().toString() + "---" + requestURI +
							"  ;请求检验的验证码类型是：" + result);
					break;
				}
			}
		}
		return result;
	}
}
