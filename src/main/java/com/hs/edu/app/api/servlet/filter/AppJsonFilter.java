package com.hs.edu.app.api.servlet.filter;


/**
 * @ClassName JsonFilter
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/13 0:06
 * @Version 1.0
 **/

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;

@WebFilter("/api/*")
@Slf4j
public class AppJsonFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
      System.out.println("拦载JSON API接口 始初化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json; charset=utf-8");
        if(log.isInfoEnabled()){
            log.info("拦载JSON API接口的访问 url地址为:{}",request.getRequestURL());
            log.info("request paramInfo:");
            log.info(JSONObject.toJSONString(request.getParameterMap()));
        }
        filterChain.doFilter(servletRequest,servletResponse);
        if(log.isInfoEnabled()){

        }
    }
    @Override
    public void destroy() {

    }
}
