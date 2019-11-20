package com.imooc.security.rbac;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义的权限表达式接口，用于决定用户是否有权限访问某些url，可以自动传入两个参数
 *
 * @author zhailiang
 */
public interface RbacService {

	boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
