package kr.opensoftlab.sdf.excel.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 * 파일 다운로드를 위한 전용 View class 이다.
 * 해당 view를 사용하기 위해서는 Controller 클래스내 메소드에서 ModelAndView 클래스를 리턴해야 한다.
 *
 * @author 조용상
 * @version 1.0
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
@Component
public class FileDownloadView extends AbstractView {
	public static final String FILE_ATTRIBUTE_NAME = "downloadFile";
	public static final String FILENAME_ATTRIBUTE_NAME = "originalFilename";
	
	private final Logger logger = Logger.getLogger(this.getClass());

	public FileDownloadView() {
		setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
	}

	/**
	 * AbstractView의 renderMergedOutputModel() 메소드를 오버라이드 함
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (model.get(FILE_ATTRIBUTE_NAME) != null) {
            InputStream is = null;
            OutputStream out = response.getOutputStream();

			try {
	            int size = -1;

	            if (model.get(FILE_ATTRIBUTE_NAME) instanceof File) {
	                File file = (File)model.get(FILE_ATTRIBUTE_NAME);
	                is = new FileInputStream(file);
	                size = (int)file.length();
	            } else {
	                is = (InputStream)model.get(FILE_ATTRIBUTE_NAME);
	            }
	            
	            String originalFilename = (String)model.get(FILENAME_ATTRIBUTE_NAME);
	            
	            // 브라우저 별 한글 깨짐 처리 ( IE11, 파폭, 크롬 )
	            String userAgent = request.getHeader("User-Agent");
	            if(userAgent.indexOf("MSIE") > -1){
	            	originalFilename = URLEncoder.encode(originalFilename, "utf-8");

	             }else{
	            	 if(userAgent.indexOf("Trident") > -1){
	            		 originalFilename = URLEncoder.encode(originalFilename, "utf-8");
	            	 }else{
	            		 originalFilename = new String(originalFilename.getBytes("utf-8"), "iso-8859-1");
	            	 }
	             }
	            
	            
	            response.setContentType(getContentType());
	            response.setContentLength(size);
	            response.setHeader("Content-Transfer-Encoding", "binary");
	            response.setHeader("Content-Disposition", "attachment; filename=\"" + originalFilename + "\";");
	            
				FileCopyUtils.copy(is, out);
			} finally {
				if(is != null){
					try{
						is.close();
					} catch(Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
			out.flush();
		}
	}
}