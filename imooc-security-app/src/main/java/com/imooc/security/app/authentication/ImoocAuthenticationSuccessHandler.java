/**
 *
 */
package com.imooc.security.app.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 适用于令牌模式的successHandler，用于在验证成功后分发令牌（OAuth2AccessToken）
 */
@Component("imoocAuthenticationSuccessHandler")
public class ImoocAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ClientDetailsService clientDetailsService;

	/**
	 * 生成OAuth2AccessToken的service接口，自动注入进来 ：实现类DefaultTokenServices
	 */
	@Autowired
	private AuthorizationServerTokenServices authorizationServerTokenServices;

	/**
	 * 作用 ：验证成功后生成 OAuth2AccessToken
	 * <p>
	 * 过程 ：
	 * <p>
	 * 1. 构建验证过的 OAuth2Request（代表合法client的请求）
	 * <p>
	 * 2. 使用OAuth2Request（代表合法client的请求）和传进来的Authentication（代表合法用户）组合成合法的OAuth2Authentication
	 * <p>
	 * 3. 以OAuth2Authentication为参数使用AuthorizationServerTokenServices生成OAuth2AccessToken
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	                                    Authentication authentication) throws IOException, ServletException {

		logger.warn("登录成功");
		//1. 构建验证过的 OAuth2Request（代表合法client）
			//1.1 生成一个clientDetails
					//a. 拿到Authorization头里面的clientId和clientSecret，basic64格式{ Authorization: Basic aW1vb2M6aW1vb2NzZWNyZXQ= }
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Basic ")) {
			throw new UnapprovedClientAuthenticationException("请求头中无client信息");
		}
					//b. 解码base64格式，获得clientId，clientSecret
		String[] tokens = extractAndDecodeHeader(header, request);
		assert tokens.length == 2;

		String clientId = tokens[0];
		String clientSecret = tokens[1];

					//c. 根据clientId查找ClientDetails
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

					//d. 检查clientDetails是否为空，或者密码是否对的上
		if (clientDetails == null) {
			throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
		} else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
			throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
		}

			//1.2 生成tokenRequest，第一个参数是用来生成userAuthentication的,其实不应该是空，应该是原参数放进去
		TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "password");

			//1.3 生成oAuth2Request
		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

		//2. 组合成合法的OAuth2Authentication
		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

		//3. 生成 OAuth2AccessToken
		OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

		//4. 把 OAuth2AccessToken 给前端
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(token));

	}

	/**
	 * 解码header信息，特定basic auth的authentication。获取里面的clientID：clientSecret
	 */
	private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, "UTF-8");

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		return new String[]{token.substring(0, delim), token.substring(delim + 1)};
	}

}
