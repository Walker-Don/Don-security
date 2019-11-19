/**
 *
 */
package com.imooc.security.core.social.weixin.connect;

import com.imooc.security.core.social.weixin.api.WeixinApi;
import com.imooc.security.core.social.weixin.api.WeixinUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 微信 api适配器，将微信 api的数据模型转为spring social的标准模型。
 * 多例
 *
 * @author zhailiang
 */
public class WeixinAdapter implements ApiAdapter<WeixinApi> {

	@Override
	public boolean test(WeixinApi api) {
		return true;
	}

	@Override
	public void setConnectionValues(WeixinApi api, ConnectionValues values) {

		WeixinUserInfo profile = api.getUserInfo();

		values.setProviderUserId(profile.getOpenid());
		values.setDisplayName(profile.getNickname());
		values.setImageUrl(profile.getHeadimgurl());
	}

	@Override
	public UserProfile fetchUserProfile(WeixinApi api) {
		return null;
	}

	@Override
	public void updateStatus(WeixinApi api, String message) {
		//do nothing
	}

}
