/**
 *
 */
package com.imooc.security.core.social.weixin.connect;

import com.imooc.security.core.social.weixin.api.WeixinApi;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;

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
     * <p>
     * createConnection--->new OAuth2Connection()--->initApi()--->serviceProvider.getApi(accessToken)
     * <p>
     * 父类结束后我们可以拿到api，把openId设回去
     *
     * @param accessGrant
     * @return
     */
    @Override
    public Connection<WeixinApi> createConnection(AccessGrant accessGrant) {
        //todo 在这里可以拿到ApiAdapter，然后设置openID
        Connection<WeixinApi> connection = super.createConnection(accessGrant);
        connection.getApi().setOpenId(connection.getKey().getProviderUserId());
        return connection;
    }

    @Override
    public Connection<WeixinApi> createConnection(ConnectionData data) {
        Connection<WeixinApi> connection = super.createConnection(data);
        connection.getApi().setOpenId(connection.getKey().getProviderUserId());
        return connection;
    }


}
