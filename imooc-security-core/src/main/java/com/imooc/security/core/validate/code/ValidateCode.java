package com.imooc.security.core.validate.code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 普通验证码的抽象对象
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName ValidateCode
 * @date 2019年10月11日 下午 5:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateCode {

    /**
     * 随机数
     */
    private String code;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * @param code      随机数
     * @param expireInt 有效时间（秒）
     */
    public ValidateCode(String code, int expireInt) {
        this(code, LocalDateTime.now().plusSeconds(expireInt));
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
