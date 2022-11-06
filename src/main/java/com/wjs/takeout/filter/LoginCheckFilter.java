package com.wjs.takeout.filter;

import com.alibaba.fastjson.JSON;
import com.wjs.takeout.common.BaseContext;
import com.wjs.takeout.common.Result;
import com.wjs.takeout.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wjs
 * @createTime 2022-10-30 21:44
 */
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      //log.info("进入过滤器");
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        //log.info("拦截到请求:{}",request.getRequestURI());
    //    1.获取本地请求的URI
        String requestURI = request.getRequestURI();
        //    2.判断本次请求是否需要处理
        //定义不需要处理的请求路径
        String urls[]=new String[]{
                "/employee/login",
                "employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/backend/page/demo/**"
        };
        for (String url :
                urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
    //    3.如果不需要处理,则直接放行
            if(match){
                filterChain.doFilter(request,response);
                return;
            }
        }
    //    4.判断登陆状态,如果已登录,则直接放行
        Employee employee = (Employee) request.getSession().getAttribute("employee");
        if(!StringUtils.isEmpty(employee)){
            BaseContext.setCurrentId(employee.getId());//通过ThreadLocal设置线程Id
            filterChain.doFilter(request,response);
            //log.info("过滤器里面当前线程Id是:{}",Thread.currentThread().getId());
        }else {
        //    5.如果未登录通过输出流的方式返回未登录结果
            response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        }
    }
}
