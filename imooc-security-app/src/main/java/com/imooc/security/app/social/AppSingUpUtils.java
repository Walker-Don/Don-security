package com.imooc.security.app.social;

import com.imooc.security.app.AppSecretException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.TimeUnit;

/**
 * 用于存取connection的中间类ConnectionData的工具
 */
@Component
public class AppSingUpUtils {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	private UsersConnectionRepository usersConnectionRepository;

	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;

	//10min
	public void saveConnectionData(WebRequest request, ConnectionData connectionData) {
		redisTemplate.opsForValue().set(getKey(request), connectionData, 10, TimeUnit.MINUTES);
	}

	/**
	 * 根据userId和request存储connection在redis中
	 */
	public void doPostSignUp(WebRequest request, String userId) {
		//1. 拿到key
		String key = getKey(request);
		if (!redisTemplate.hasKey(key)) {
			throw new AppSecretException("无法找到缓存的用户社交账号信息");
		}
		//2. redis中拿到connectionData
		ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);
		//3. 根据connectionData创建Connection
		Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId())
				.createConnection(connectionData);
		//4. 根据userId创建为专属这个user的控制connection的connectionRepository
		usersConnectionRepository.createConnectionRepository(userId).addConnection(connection);
		//5. 存储成功，删除该data
		redisTemplate.delete(key);
	}

	/**
	 * 从request的header中获取一个唯一的设备id
	 */
	private String getKey(WebRequest request) {
		String deviceId = request.getHeader("deviceId");
		if (StringUtils.isBlank(deviceId)) {
			throw new AppSecretException("设备id参数不能为空");
		}
		return "imooc:security:social.connect." + deviceId;
	}

}
