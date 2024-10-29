package model2.mvcboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;

import java.io.IOException;

import fileupload.FileUtil;

@WebServlet("/mvcboard/edit.do")
@MultipartConfig(
	maxFileSize = 1024*1024*1,
	maxRequestSize = 1024*1024*10			
)
public class EditController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idx = request.getParameter("idx");
		
		MVCBoardDAO dao = new MVCBoardDAO();
		MVCBoardDTO dto = dao.selectView(idx);
		
		request.setAttribute("dto", dto);
		request.getRequestDispatcher("/14MVCBoard/Edit.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//1. 파일 업로드 처리
		String saveDirectory = request.getServletContext().getRealPath("/Uploads");
		
		String originalFileName = "";
		try {
			originalFileName = FileUtil.uploadFile(request, saveDirectory);
		}catch(Exception e) {
			JSFunction.alertBack(response, "파일 업로드 오류입니다.");
			return;
		}
		
		//2. 파일 업로드 외 처리
		String idx = request.getParameter("idx");
		
		//이미지 수정하면 기존 이미지 ofile, sfile가지고 있다가
		//삭제 해야한다.
		String prevOfile = request.getParameter("prevOfile");
		String prevSfile = request.getParameter("prevSfile");
		
		String name = request.getParameter("name");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		//비밀번호를 session에서 가져오기
		HttpSession session = request.getSession();
		String pass = (String)session.getAttribute("pass");
		
		//DTO저장
		MVCBoardDTO dto = new MVCBoardDTO();
		dto.setIdx(idx);
		dto.setName(name);
		dto.setTitle(title);
		dto.setContent(content);
		dto.setPass(pass);
		
		
		if(originalFileName != "") {
			// 첨부 파일 변경
			String savedFileName = 
					FileUtil.renameFile(saveDirectory, originalFileName);
			dto.setOfile(originalFileName);
			dto.setSfile(savedFileName);
			
			//기존 파일 삭제
			FileUtil.deleteFile(request, "/Uploads", prevSfile);
		}else {
			// 첨부파일 변경하지 않음.
			dto.setOfile(prevOfile);
			dto.setSfile(prevSfile);
		}
		
		//DB에 수정 내용 반영
		MVCBoardDAO dao = new MVCBoardDAO();
		int result = dao.updatePost(dto);
		
		if(result == 1) { //수정 성공
			session.removeAttribute("pass");
			response.sendRedirect("../mvcboard/view.do?idx="+idx);
		}else { //수정 실패
			JSFunction.alertLocation(response, "비밀번호 검증을 다시 진행해주세요.",
					"../mvcboard/view.do?idx"+ idx);
		}
	}

}
