package com.imooc.security.core.validate.code.processor;

import com.imooc.security.core.validate.code.codeGenerator.ValidateCodeGenerator;
import com.imooc.security.core.validate.code.model.ValidateCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * ValidateCodeProcessor的一个处理规范，generate和save是一样的，因此抽象在AbstractValidateCodeProcessor这一层
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月13日 下午 2:10
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

  //session操作工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
     */
    @Autowired//靠子类也能注入？
    private Map<String, ValidateCodeGenerator> validateCodeGeneratorMap;

    @Override

    /**
     * generate和save是一样的，因此抽象在AbstractValidateCodeProcessor这一层
     */
    public void create(ServletWebRequest servletWebRequest) throws Exception {
        C validateCode = generate(servletWebRequest);//todo 感觉这一层不应该在这里实现，
        save(servletWebRequest, validateCode);
        send(servletWebRequest, validateCode);
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
        return (C) validateCodeGeneratorMap.get(getProcessorType(servletWebRequest) + "CodeGenerator").generateCode(servletWebRequest);
    }

    /**
     * 把validateCode保存到session中
     *
     * @param servletWebRequest
     * @param validateCode
     */
    private void save(ServletWebRequest servletWebRequest, C validateCode) {
        sessionStrategy.setAttribute(servletWebRequest, ValidateCodeProcessor.SESSION_KEY_PREFIX + getProcessorType(servletWebRequest), validateCode);

    }

    /**
     * 根据请求的url获取校验码的类型
     *
     * @param servletWebRequest
     * @return
     */
    private String getProcessorType(ServletWebRequest servletWebRequest) {
        return StringUtils.substringAfter(servletWebRequest.getRequest().getRequestURI(), "/code/");
    }
}
