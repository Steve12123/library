package com.hkjxth.configuration;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录功能拦截器
 */
public class LoginControllerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object username=request.getSession().getAttribute("loginUser");
        if(username==null){
            //未登录，返回登录页面
            request.setAttribute("loginErrInfoToUser","请先登录");
            request.getRequestDispatcher("/").forward(request,response);
            return false;
        }else{
                return true;
        }

    }
}
