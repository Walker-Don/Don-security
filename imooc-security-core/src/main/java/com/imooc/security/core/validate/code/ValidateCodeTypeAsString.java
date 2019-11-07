package com.imooc.security.core.validate.code;

/**
 * 枚举类ValidateCodeType对应的String
 * <p>
 * 没有用这个类，可以有这种用法
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月16日 上午 11:32
 */
public final class ValidateCodeTypeAsString {

	public static final String SMS;
	public static final String IMAGE;

	static {
		SMS = ValidateCodeType.SMS.toString();
		IMAGE = ValidateCodeType.IMAGE.toString();
	}

	public static void main(String[] args) {
		System.out.println(SMS);
	}
}
