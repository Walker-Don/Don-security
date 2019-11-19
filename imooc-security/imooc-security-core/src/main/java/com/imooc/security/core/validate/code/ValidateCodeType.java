package com.imooc.security.core.validate.code;

import java.util.Arrays;

/**
 * ValidateCodeType,并且返回ValidateCodeType对应的request请求中的参数name
 * <p>
 * 也可以在其它类中直接用String表示
 * <p>
 * enum与String息息相关，互相转化
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月15日 下午 7:31
 */

public enum ValidateCodeType {

	SMS

			{
				@Override
				public String getParamNameOnValidate() {
					return "smsCode";
				}
			},

	IMAGE

			{
				@Override
				public String getParamNameOnValidate() {
					return "imageCode";
				}
			};

	/**
	 * 返回对应的ValidateCodeType在request请求中的参数name
	 *
	 * @return name of validate among request params
	 */
	public abstract String getParamNameOnValidate();

	public static void main(String[] args) {
		System.out.println(ValidateCodeType.SMS);//打印enum：SMS
		ValidateCodeType validateCodeType = ValidateCodeType.valueOf("SMS");//创建enum，大小写必须一样
		System.out.println(validateCodeType);
		System.out.println(Arrays.toString(ValidateCodeType.values()));//显示所有enum，[SMS, IMAGE]
	}
}
