package org.ramer.diary.filter;

import java.io.IOException;

import javax.servlet.*;

/**
 * @author R 
 * 编码过滤器
 */
//@WebFilter(urlPatterns = { "/*" })
public class EncodingFilter implements Filter {

 @Override
 public void destroy() {
 }

 @Override
 public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
   throws IOException, ServletException {
  System.out.println("编码过滤器");
  request.setCharacterEncoding("UTF-8");
  response.setCharacterEncoding("UTF-8");
  filterChain.doFilter(request, response);
 }

 @Override
 public void init(FilterConfig arg0) throws ServletException {
 }

}
