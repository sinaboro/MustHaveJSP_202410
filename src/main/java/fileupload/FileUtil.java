package fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class FileUtil {
	
	//파일 업로드
	public static String uploadFile(HttpServletRequest request, String sDirectory) 
					throws ServletException, IOException{
		
//		Part part = request.getPart("ofile");
		Part part = request.getPart("attachedFile");
		String partHeader = part.getHeader("content-disposition");
		System.out.println("partHeader : " + partHeader);
		
		String[] phArr = partHeader.split("filename=");
		
		System.out.println("------------------");
		for(String s : phArr)
			System.out.println(s);
		System.out.println("------------------");
		
		String originalFileName = phArr[1].trim().replace("\"", "");
		System.out.println("originalFileName : " + originalFileName);
	
		if(!originalFileName.isEmpty()) {
			part.write(sDirectory + File.separator + originalFileName); 
		}		
		return originalFileName;		
	}
	
	//파일명 변경
	public static String renameFile(String sDirectory, String fileName) {
		
		String ext = fileName.substring(fileName.lastIndexOf("."));
		System.out.println("ext : " + ext);
		
		String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
		String newFileName = now+ext;
		
		File oldFile = new File(sDirectory + File.separator + fileName);
		File newFile = new File(sDirectory + File.separator + newFileName);
		oldFile.renameTo(newFile);
		
		return newFileName;
	}

	public static List<String> multipleFile(HttpServletRequest request, String saveDirectory) throws IOException, ServletException {
		
		System.out.println("-------------multipleFile----------------------");
		List<String> listfileName = new ArrayList<String>();
		
		Collection<Part> parts = request.getParts();
		
		for(Part part : parts) {
			if(!part.getName().equals("attachedFile"))	continue;
			
			String partHeader = part.getHeader("content-disposition");
		
			String[] phArr = partHeader.split("filename=");
			String originalfileName = phArr[1].trim().replace("\"", "");
		
			if(!originalfileName.isEmpty()) {
				part.write(saveDirectory + File.separator + originalfileName);
			}
			listfileName.add(originalfileName);
		}
		
		return listfileName;
	}
	
	//명시한 파일을 찾아 다운로드합니다.
	public static void download(HttpServletRequest request, HttpServletResponse response, 
				String directory, String sfileName, String ofileName) {
		
		String  sDirectory = request.getServletContext().getRealPath(directory);
		System.out.println("download sDirectory : " + sDirectory);
		try {
			
			File file =  new File(sDirectory, sfileName);
			InputStream iStream = new FileInputStream(file);
			
			
			String encodedFileName = URLEncoder.encode(ofileName, "UTF-8").replaceAll("\\+", "%20");
			//파일 다운로드용 응답 헤더 설정
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
			response.setHeader("Content-Length", ""+file.length());
			
			
			//response 내장 객체로부터 새로운 출력 스트림 생성
			OutputStream oStream = response.getOutputStream();
			
			//출력 스트림에 파일 내용 출력
			byte[] b = new byte[(int)file.length()];
			int readBuffer = 0;
			
			while( (readBuffer = iStream.read(b)) > 0) {
				oStream.write(b, 0,readBuffer);
			}
			
			iStream.close();
			oStream.close();
			
		}catch(FileNotFoundException e) {
			System.out.println("해당 파일을 찾을 수 없습니다.");
			e.printStackTrace();
		}catch(Exception e) {
			System.out.println("예외가 발생하였습니다.");
			e.printStackTrace();
		}
	}
	
	//지정한 위치의 파일을 삭제합니다.
	public static void deleteFile(HttpServletRequest request, String directory, String filename) {
		
		String sDirectory = request.getServletContext().getRealPath(directory);
		System.out.println("sDirectory : " + sDirectory);
		File file = new File(sDirectory +File.separator + filename);
		
		if(file.exists()) {
			file.delete();
		}
	}
}







