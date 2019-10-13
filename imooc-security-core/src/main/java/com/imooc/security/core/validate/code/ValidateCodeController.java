package com.imooc.security.core.validate.code;

import com.imooc.security.core.validate.code.codeGenerator.ImageCodeGenerator;
import com.imooc.security.core.validate.code.codeGenerator.SmsCodeGenerator;
import com.imooc.security.core.validate.code.model.ImageCode;
import com.imooc.security.core.validate.code.model.ValidateCode;
import com.imooc.security.core.validate.code.smsSender.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求生成验证码,请求url需要在BrowserSecurityConfig中配置放行
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName ValidateCodeController
 * @date 2019年10月11日 下午 5:28
 */
@RestController
public class ValidateCodeController {
    public static final String SESSION_KEY_IMAGE_CODE = "SESSION_KEY_IMAGE_CODE";
    public static final String SESSION_KEY_SMS_CODE = "SESSION_KEY_SMS_CODE";

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private SmsCodeGenerator smsCodeGenerator;

    @Autowired
    private ImageCodeGenerator imageCodeGenerator;

    @Autowired
    private SmsCodeSender smsCodeSender;

    /**
     * 生成图形验证码，并且存储在session中，以供下一次触发ValidateCodeFilter的请求使用
     *
     * @param request
     * @param response
     */
    @GetMapping("/code/image")
    public void getImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        // 1. 根据随机数生成图片
        ImageCode imageCode = imageCodeGenerator.createCode(servletWebRequest);
        // 2. 将随机数存到 Session中 todo 是否应该存入数据库？
        sessionStrategy.setAttribute(servletWebRequest, SESSION_KEY_IMAGE_CODE, imageCode);
        // 3. 将生成的图片写到接口的响应中
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

    @GetMapping("/code/sms")
    public void getSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        // 1. 生成smsCode
        ValidateCode smsCode = smsCodeGenerator.createCode(servletWebRequest);
        // 2. 将smsCode存到 Session中 todo 是否应该存入数据库？
        sessionStrategy.setAttribute(servletWebRequest, SESSION_KEY_SMS_CODE, smsCode);
        // 3. 取出request中的手机电话号码
        String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");
        // 4. 发送短信
        smsCodeSender.send(mobile, smsCode.getCode());
    }

}
