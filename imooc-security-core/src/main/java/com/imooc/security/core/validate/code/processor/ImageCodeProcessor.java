package com.imooc.security.core.validate.code.processor;

import com.imooc.security.core.validate.code.model.ImageCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;

/**
 * 最终处理imageCode的bean类
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月13日 下午 4:52
 */
@Component
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    public static final String SESSION_KEY_FOR_CODE_IMAGE = "SESSION_KEY_FOR_CODE_IMAGE";

    /**
     * 将生成的图片写到接口的响应中
     * @param servletWebRequest
     * @param imageCode
     * @throws Exception
     */
    @Override
    protected void send(ServletWebRequest servletWebRequest, ImageCode imageCode) throws Exception {
        ImageIO.write(imageCode.getImage(), "JPEG", servletWebRequest.getResponse().getOutputStream());
    }
}
