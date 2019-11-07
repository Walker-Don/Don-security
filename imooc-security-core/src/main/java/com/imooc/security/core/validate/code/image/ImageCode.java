package com.imooc.security.core.validate.code.image;

import com.imooc.security.core.validate.code.ValidateCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 图片验证码的抽象对象
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName ValidateCode
 * @date 2019年10月11日 下午 5:09
 */
@Data
//@AllArgsConstructor()//不可以创建包含并初始化父类fields的构造器,所以没用
@NoArgsConstructor
public class ImageCode extends ValidateCode implements Serializable {

	/**
	 * 根据随机数生成的图片
	 */
	private BufferedImage image;

	/**
	 * @param image     根据随机数生成的图片
	 * @param code      随机数
	 * @param expireInt 有效时间（秒）
	 */
	public ImageCode(BufferedImage image, String code, int expireInt) {
		super(code, expireInt);
		this.image = image;
	}

	public ImageCode(BufferedImage image, String code, LocalDateTime localDateTime) {
		super(code, localDateTime);
		this.image = image;
	}

}
