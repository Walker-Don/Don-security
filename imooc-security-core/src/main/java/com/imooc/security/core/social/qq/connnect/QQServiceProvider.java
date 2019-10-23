package com.imooc.security.core.social.qq.connnect;

import com.imooc.security.core.social.qq.api.QQApi;
import com.imooc.security.core.social.qq.api.DefaultQQApi;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * QQSocial相关的serviceProvider
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月18日 下午 12:20
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQApi> {

    public String appId;
    public String appSecret;
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    /**
     * Create a new {@link AbstractOAuth2ServiceProvider}.
     */
    public QQServiceProvider(String appId, String appSecret) {//todo appId appSecret是固定的，不需要作为参数
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQApi getApi(String accessToken) {
        return new DefaultQQApi(accessToken, appId);
    }
}
