package com.imooc.security.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

/**
 * AbstractOAuth2ApiBinding的实现子类，处理第六步的api类
 * <p>
 * 多例
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月18日 下午 12:21
 */
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {
    //发送http请求获取openId，accessToken是个性化的
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    //发送http请求获取UserInfo
    private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String appId; //也就是oauth_consumer_key，申请QQ登录成功后，分配给应用的appId

    private String openId;//用户的ID，与QQ号码一一对应。

    private ObjectMapper objectMapper = new ObjectMapper();//转换对象和json

    /**
     * 初始化所有参数，传入accessToken，还没获取UserInfo
     *
     * @param accessToken
     * @param appId
     */
    public QQImpl(String accessToken, String appId) {
        //1. 把accessToken传入父类，以param的形式加在url后（自动的）
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);

        //2. 初始化appId todo appId应用级别，全局唯一，应该在开始读取设置，不应该构造器传入
        this.appId = appId;

        //3. 获取openId
        //  3.1 拼接openId_url，个性化accessToken
        String openId_url = String.format(URL_GET_OPENID, accessToken);
        //  3.2 发送请求
        String openId_url_result = getRestTemplate().getForObject(openId_url, String.class);
        //  3.3 读取结果中的openId
        System.out.println(openId_url_result);
        this.openId = StringUtils.substringBetween(openId_url_result, "\"openid\":", "}");
    }

    /**
     * 获取用户信息，根据qq互联给的url，三个参数，access_token（不需要），appId，openId
     *
     * @return
     * @throws Exception
     */
    @Override
    public QQUserInfo getUserInfo() {
        //1. 拼接userInfo_url         todo 为什么这里不需要拼接accessToken，而上面的 openId_url需要拼接？
        String userInfo_url = String.format(URL_GET_USERINFO, appId, openId);
        //2. 发送请求
        String result = getRestTemplate().getForObject(userInfo_url, String.class);
        //3. 读取结果，转换为QQUserInfo
        System.out.println(result);
        try {
            return objectMapper.readValue(result, QQUserInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}
