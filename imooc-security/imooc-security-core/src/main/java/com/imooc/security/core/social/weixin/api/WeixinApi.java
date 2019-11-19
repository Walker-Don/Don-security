/**
 *
 */
package com.imooc.security.core.social.weixin.api;

/**
 * 微信API调用接口
 *
 * @author zhailiang
 */
public interface WeixinApi {

	//todo 能否在weixinApi中设置openId这个field，然后在后面拿到openId后再设置上去？

	//传入openID(qq没有)是因为微信不需要用Token换openID，也可能没有这个接口，因此放在Adapter中，其实就是因为不能设置openId这个field
	WeixinUserInfo getUserInfo();

	void setOpenId(String openId);

}
