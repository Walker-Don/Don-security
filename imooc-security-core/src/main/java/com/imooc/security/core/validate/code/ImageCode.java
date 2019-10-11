package com.imooc.security.core.validate.code;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;
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
@AllArgsConstructor
public class ImageCode {

    /**
     * 根据随机数生成的图片
     */
    private BufferedImage image;

    /**
     * 随机数
     */
    private String code;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     *
     * @param image 根据随机数生成的图片
     * @param code 随机数
     * @param expireInt 有效时间（秒）
     */
    public ImageCode(BufferedImage image, String code, int expireInt) {
        this(image, code, LocalDateTime.now().plusSeconds(expireInt));

    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

}
