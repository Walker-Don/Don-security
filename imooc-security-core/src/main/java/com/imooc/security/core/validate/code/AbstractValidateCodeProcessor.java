package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.validate.code.image.ImageCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * 中间层
 * ValidateCodeProcessor的一个处理规范，generate和save是一样的，因此抽象在AbstractValidateCodeProcessor这一层
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月13日 下午 2:10
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    /**
     * 操作session的工具类
     */

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    /**
     * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
     */
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGeneratorMap;

    @Override

    /**
     * generate和save是一样的，因此抽象在AbstractValidateCodeProcessor这一层
     */
    public void create(ServletWebRequest servletWebRequest) throws Exception {
        //1. generate ValidateCode
        C validateCode = generate(servletWebRequest);//todo 感觉这一层不应该在这里实现，
        //2. save to session
        save(servletWebRequest, validateCode);
        //3. send to customer(sms,web)
        send(servletWebRequest, validateCode);
    }

    /**
     * 在这里统一validate各种code，三步走，只有session_key和request_param_name不同，这个处理是关键
     *
     * @param servletWebRequest
     */
    @SuppressWarnings("unchecked")
    @Override
    public void validate(ServletWebRequest servletWebRequest) {
        ValidateCodeType validateCodeType = getValidateCodeType(servletWebRequest);
        //1. 拿到session中的code
        String sessionKey = getSessionKey(servletWebRequest);
        C smsCodeInSession = (C) sessionStrategy.getAttribute(servletWebRequest, sessionKey);

        //2. 拿到request中的code
        String codeInRequest;
        try {//一定没有错的
            codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),
                    validateCodeType.getParamNameOnValidate());
        }
        //3. 比较以及结果处理
        catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (smsCodeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (smsCodeInSession.isExpired()) {
            sessionStrategy.removeAttribute(servletWebRequest, sessionKey);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(smsCodeInSession.getCode(), codeInRequest)) {
            sessionStrategy.removeAttribute(servletWebRequest, sessionKey);
            throw new ValidateCodeException("验证码不匹配");
        }
        sessionStrategy.removeAttribute(servletWebRequest, sessionKey);
    }

    /**
     * 发送，类给子类实现
     *
     * @param servletWebRequest
     * @param validateCode
     */
    protected abstract void send(ServletWebRequest servletWebRequest, C validateCode) throws Exception;

    /**
     * 调用ValidateCodeGenerator接口生成ValidateCode
     *
     * @param servletWebRequest
     * @return
     */
    @SuppressWarnings("unchecked")
    private C generate(ServletWebRequest servletWebRequest) {
        String type = getValidateCodeType(servletWebRequest).toString().toLowerCase();
        String generatorBeanName = type + ValidateCodeGenerator.class.getSimpleName();

        ValidateCodeGenerator validateCodeGenerator = validateCodeGeneratorMap.get(generatorBeanName);
        if (validateCodeGenerator == null) {
            throw new ValidateCodeException("验证码生成器" + generatorBeanName + "不存在");
        }
        return (C) validateCodeGenerator.generateCode(servletWebRequest);
    }

    /**
     * 把validateCode保存到session中
     *
     * @param servletWebRequest
     * @param validateCode
     */
    private void save(ServletWebRequest servletWebRequest, C validateCode) {
        //热部署jettyServer不能持久化BufferedImage，有异常，设为null解决
        if (ImageCode.class.isInstance(validateCode)) {
             validateCode =(C) new ImageCode(null,validateCode.getCode(),validateCode.getExpireTime());
        }
        sessionStrategy.setAttribute(servletWebRequest, getSessionKey(servletWebRequest), validateCode);

    }

    /**
     * 构建验证码放入session时的key
     *
     * @param request
     * @return
     */
    private String getSessionKey(ServletWebRequest request) {
        return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
    }

    /**
     * 类似getValidateCodeTypeAsString
     * <p>
     * 根据当前实现类对象找出ValidateCodeType
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "ValidateCodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    /**
     * 根据请求的url获取校验码的类型
     * /code/sms , /code/image
     *
     * @param servletWebRequest
     * @return
     */
    private String getValidateCodeTypeAsString(ServletWebRequest servletWebRequest) {
        return StringUtils.substringAfter(servletWebRequest.getRequest().getRequestURI(),
                SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/");
    }
}
