package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import memberShip.MemberDAO;
import memberShip.MemberDTO;
import utils.JSFunction;

@WebFilter(filterName = "LoginFilter", urlPatterns = "/15FilterListener/LoginFilter.jsp")
public class LoginFilter implements Filter {

	String orlcleDriver, orlaleURL, oracleId, oraclePwd;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext application = filterConfig.getServletContext();
		orlcleDriver = application.getInitParameter("OracleDriver");
		orlaleURL = application.getInitParameter("OracleURL");
		oracleId = application.getInitParameter("OralceId");
		oraclePwd = application.getInitParameter("OraclePwd");
		
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest requset = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		
		HttpSession session = requset.getSession();
		String method = requset.getMethod();
		
		if(method.equals("POST")) {
			String user_id = requset.getParameter("user_id");
			String user_pwd = requset.getParameter("user_pwd");
			
			MemberDAO dao = new MemberDAO(orlcleDriver, orlaleURL, oracleId, oraclePwd);
			MemberDTO memberDTO = dao.getMemberDTO(user_id, user_pwd);
			
			dao.close();
			
			if(memberDTO.getId() != null) {
				//세션에 로그인 정보 저장
				session.setAttribute("UserId", memberDTO.getId());
				session.setAttribute("UserName", memberDTO.getName());

				//다음 페이지 이동
				String backUrl = requset.getParameter("backUrl");
				
				if(backUrl != null && !backUrl.equals("")) {
					JSFunction.alertLocation(response, "로그인 전 요청한 페이지로 이동합니다.", backUrl);
					return;
				}else {
					response.sendRedirect("../15FilterListener/LoginFilter.jsp");
				}
			}else {  //로그인 실패
				requset.setAttribute("LoginErrMsg", "로그인에 실패했습니다.");
				requset.getRequestDispatcher("../15FilterListener/LoginFilter.jsp").forward(requset, response);
			}
		}else if(method.equals("GET")){  //로그아웃 처리
			String mode = requset.getParameter("mode");
			if(mode != null && mode.equals("logout")) {
				session.invalidate();   //세선 저장한 정보 삭제
			}
		}
		
		chain.doFilter(requset, response);
	}
}
