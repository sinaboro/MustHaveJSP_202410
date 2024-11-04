package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter(filterName = "AnnoFilter", urlPatterns = "/15FilterListener/AnnoFilter.jsp")
public class AnnoFilter implements Filter{
	
	
	@Override
	public void doFilter(ServletRequest requset, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String searchField = requset.getParameter("searchField");
		String searchWord = requset.getParameter("searchWord");
		
		System.out.println("검색 필드 : " + searchField);
		System.out.println("검색 어 : " + searchWord);
		
		chain.doFilter(requset, response);
	}
	

}
