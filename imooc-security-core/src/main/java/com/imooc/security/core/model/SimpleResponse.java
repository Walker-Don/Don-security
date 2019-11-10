package com.imooc.security.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 适用于任何返回类型
 *
 * @author Walker_Don
 * @version V1.0
 * @ClassName SimpleResponse
 * @date 2019年10月10日 下午 3:24
 */
@Data
@AllArgsConstructor
public class SimpleResponse {
	private Object content;
}
