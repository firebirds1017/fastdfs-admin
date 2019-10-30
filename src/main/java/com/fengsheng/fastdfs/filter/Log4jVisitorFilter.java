package com.fengsheng.fastdfs.filter;

import com.fengsheng.fastdfs.utils.IpUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Log4jVisitorFilter", urlPatterns = "/*")
public class Log4jVisitorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String userIP = IpUtils.getClientIpAddress(request);
        String referer = request.getHeader("referer");

        ThreadContext.put("userIP", userIP);
        ThreadContext.put("referer", referer);
        System.err.println("==================================");
        System.err.println("登录IP：" + userIP);
        System.err.println("referer：" + referer);
        System.err.println("==================================");
        filterChain.doFilter(request, response);
    }
}
