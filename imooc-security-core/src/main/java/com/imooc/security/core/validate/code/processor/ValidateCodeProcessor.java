package com.imooc.security.core.validate.code.processor;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 管理生成、保存、发送的
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月13日 下午 1:52
 */
public interface ValidateCodeProcessor {

    /**
     * 验证码放入session时的前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    /**
     * 创建校验码
     *
     * @param servletWebRequest
     * @throws Exception
     */
    void create(ServletWebRequest servletWebRequest) throws Exception;
}
