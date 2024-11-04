package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class BasicFilter implements Filter{
	
	FilterConfig config;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		config = filterConfig;
		
		String filterName = filterConfig.getFilterName();
		
		System.out.println("BasicFilter -> init() 호출됨 : " + filterName);
	}
	
	@Override
	public void doFilter(ServletRequest requset, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String method = ((HttpServletRequest)requset).getMethod();
		System.out.println("BasicFilter -> 전송방식 : " + method);
		
		//반드시 기입 -> 기입하지않으면 진행 중단..
		chain.doFilter(requset, response);
	}
	
	
	@Override
	public void destroy() {
		System.out.println("BasicFilter -> destroy() 호출됨");
	}


}
