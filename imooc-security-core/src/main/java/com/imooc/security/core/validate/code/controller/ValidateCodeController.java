package com.imooc.security.core.validate.code.controller;

import com.imooc.security.core.validate.code.processor.ValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessorMap;

    /**
     * 创建验证码，根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor}接口实现
     * @param request
     * @param response
     * @param type
     * @throws Exception
     */
    @GetMapping("/code/{type}")//type大小写要对上，拿bean的时候
    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable("type") String type)
            throws Exception {
        validateCodeProcessorMap.get(type+"CodeProcessor").create(new ServletWebRequest(request, response));
    }

}
