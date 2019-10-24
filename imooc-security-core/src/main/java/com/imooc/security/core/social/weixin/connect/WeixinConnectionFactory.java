/**
 *
 */
package com.imooc.security.core.social.weixin.connect;

import com.imooc.security.core.social.weixin.api.WeixinApi;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * 微信连接工厂
 *
 * @author zhailiang
 */
public class WeixinConnectionFactory extends OAuth2ConnectionFactory<WeixinApi> {

    public WeixinConnectionFactory(String providerId, String appId, String appSecret) {
        //创建ConnectionFactory时还没知道openId是什么，但是返回的时候需要返回特定的Adapter(多例)
        super(providerId, new WeixinServiceProvider(appId, appSecret), new WeixinAdapter());
    }

    /**
     * 由于微信的openId是和accessToken一起返回的，所以在这里直接根据accessToken设置providerUserId即可，不用像QQ那样通过QQAdapter来获取
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof WeixinAccessGrant) {
            return ((WeixinAccessGrant) accessGrant).getOpenId();
        }
        return null;
    }

    /**
     * 创建的connection需要accessGrant，而我们已经重写了，添加了OpenId
     *
     * @param accessGrant
     * @return
     */
    @Override
    public Connection<WeixinApi> createConnection(AccessGrant accessGrant) {
        //todo 在这里可以拿到ApiAdapter，然后设置openID
        return new OAuth2Connection<WeixinApi>(getProviderId(), extractProviderUserId(accessGrant),
                accessGrant.getAccessToken(), accessGrant.getRefreshToken(),
                accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
    }

    @Override
    public Connection<WeixinApi> createConnection(ConnectionData data) {
        return new OAuth2Connection<WeixinApi>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
    }

    /**
     * 每个openId都有一个对应的ApiAdapter<WeixinApi>
     * @param providerUserId
     * @return
     */
    private ApiAdapter<WeixinApi> getApiAdapter(String providerUserId) {
        return new WeixinAdapter(providerUserId);
    }

    /**
     * 私有方法需要重写，因为没办法从父类拿回来
     * @return
     */
    private OAuth2ServiceProvider<WeixinApi> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<WeixinApi>) getServiceProvider();
    }

}
