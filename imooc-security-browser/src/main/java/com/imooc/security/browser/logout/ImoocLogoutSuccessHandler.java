package com.imooc.security.browser.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.security.browser.model.SimpleResponse;
import com.imooc.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 我自己的logoutHandler，主要是做一些日志操作，然后重定向到html（如果有），或者直接发送成功的json信息
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年11月08日 下午 8:29
 */
public class ImoocLogoutSuccessHandler implements LogoutSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SecurityProperties securityProperties;

	private ObjectMapper objectMapper = new ObjectMapper();

	//给他一个构造器把securityProperties传进入
	public ImoocLogoutSuccessHandler(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		//focus 试试如何从中拿出user的信息
		logger.warn(request.getSession().getId() + "退出登陆");

		//配置了logOutSuccess page那么就发送这个html，否则发送json
		String logoutSuccessPage = securityProperties.getBrowser().getLogoutSuccessPage();
		if (logoutSuccessPage.equals("-")) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("退出成功")));
		} else {
			response.sendRedirect(logoutSuccessPage);
		}
	}
}
