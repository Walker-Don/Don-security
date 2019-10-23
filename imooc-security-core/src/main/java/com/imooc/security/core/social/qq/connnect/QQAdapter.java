/**
 *
 */
package com.imooc.security.core.social.qq.connnect;

import com.imooc.security.core.social.qq.api.QQApi;
import com.imooc.security.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 通用的，单例
 *
 * @author zhailiang
 */
public class QQAdapter implements ApiAdapter<QQApi> {

    @Override
    public boolean test(QQApi api) {
        return true;
    }

    //调用api binding 设置值给ConnectionValues
    @Override
    public void setConnectionValues(QQApi api, ConnectionValues values) {

        QQUserInfo userInfo = api.getUserInfo();

        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        values.setProfileUrl(null);
        values.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQApi api) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateStatus(QQApi api, String message) {
        //do noting
    }

}
