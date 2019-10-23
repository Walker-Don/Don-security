package com.imooc.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.imooc.dto.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    /**
     * 处理用户注册的页面
     *
     * @param user
     */
    @PostMapping("/regist")
    public void regist(HttpServletRequest request, User user) {

        //根据用户的选择进行用户注册或绑定，不论进行哪种操作（crud），最后都应该得到一个用户的唯一标识，在
        //处理登陆......

        //这里我们使用用户填写的用户名作为唯一标识来绑定服务提供商用户
        String userId = user.getUsername();
        //把userId传给springSocial，然后和openId、其他数据一起插入到'userconnection'这个表中去，从request中拿session的connection
        providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));

        //..注册成功就自动用数据登陆，然后重定向(或者把Authentication放进contextholder中)


    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        logger.info(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));

//        if (errors.hasErrors()) {
//            errors.getAllErrors().forEach(e -> logger.error(e.getDefaultMessage()));
//        }

        return user.setId("1");
    }

    @PutMapping("/{id:\\d+}")
    public User update(@Valid @RequestBody User user, BindingResult errors) {
        logger.info(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> logger.error(e.getDefaultMessage()));
        }

        return user;
    }

    @GetMapping
    @JsonView(User.UserSimpleView.class)
    @ApiOperation(value = "用户查询服务")
    public List<User> query(User user, @PageableDefault(size = 10, page = 0) Pageable pageable) {

        // 通过反射方法，打印查询参数对象
        logger.info(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));
        logger.info(ReflectionToStringBuilder.toString(pageable, ToStringStyle.MULTI_LINE_STYLE));

        ArrayList<User> users = new ArrayList<>();
        users.add(new User().setUsername("user1").setPassword("1"));
        users.add(new User().setUsername("user2").setPassword("2"));
        users.add(new User().setUsername("user3").setPassword("3"));
        return users;
    }

    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getInfo(@ApiParam(value = "用户ID") @PathVariable String id) {
//        throw new UserNotExistException(id);

        logger.info("getInfo user_id = {}", id);
        return new User().setUsername("user1").setPassword("1");

    }

    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id) {
        logger.info("delete user_id = {}", id);
    }

    @GetMapping("/me1")
    public Object getCurrentUser(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/me2")
    public Object getCurrentUser2() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/me/details1")
    public Object getCurrentUser1(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

}
