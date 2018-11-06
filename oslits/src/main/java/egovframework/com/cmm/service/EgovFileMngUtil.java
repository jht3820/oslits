package egovframework.com.cmm.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.util.EgovResourceCloseHelper;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

/**
 * @Class Name  : EgovFileMngUtil.java
 * @Description : 메시지 처리 관련 유틸리티
 * @Modification Information
 *
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.02.13       이삼섭                  최초 생성
 *   2011.08.09       서준식                  utl.fcc패키지와 Dependency제거를 위해 getTimeStamp()메서드 추가
 *   2016.02.15		    진주영		   파일 업로드 부분 FormData형식 추가
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 02. 13
 * @version 1.0
 * @see
 *
 */
@Component("EgovFileMngUtil")
public class EgovFileMngUtil {

	public static final int BUFF_SIZE = 2048;

	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;
	
	/**
	 * 파일 업로드 처리
	 * @param request MultipartHttpServletRequest content-type:multipart/form-data 필수
	 * @return  파일첨부 완료시 atchFileId값 리턴
	 * 			에러 발생 시 'ERROR' 리턴
	 * 			첨부하려는 파일이 없을 경우 'NULL' 리턴
	 * @decription
	 * [JSP SCRIPT 설정 시]
	 * 	dragndropDiv 		- Drag&Drop (fileUpload.js)를 이용한 파일 첨부 시, 첨부된 파일 목록 DIV의 ID (개별 삭제X, 전체 삭제O)
	 * 	dragandrophandler 	- Drag&Drop의 영역을 지정하려는 div의 id값 
	 * 	egovFileUpload 		- input type='file'의 id값
	 * 	status1				- 파일 선택으로 추가된 파일 목록 (개별 삭제 가능)
	 * [필요 파일]
	 * 	fileUpload.js
	 * 	fileUpload.css
	 * 	EgovMultiFile.js
	 * [필수]
	 *	FormData 전송 방식이기 때문에 contentType: multipart/form-data이 기본값
	 * 	AJAX에서 contentType: false 로 옵션 설정 필수!
	 * [동작 예]
	 * TextFile1.txt , TextFile2.txt를 동시에 업로드 했다고 가정할 경우
	 * 두 파일 atchFileId에 OSL000001 부여
	 * 첨부된 파일 불러오기에는 FILE_SN 값으로 분류한다. TextFile1.txt - file_sn=0, TextFile2.txt - file_sn=1
	 */
	public List<FileVO> fileUploadInsert(MultipartHttpServletRequest request,String inputAtchFileId, int inputFileSn, String addStrPath) throws Exception{
		//파일 정보 담을 변수
   		List<FileVO> _result = null;
   		
		inputFileSn = (inputFileSn<0)?0:inputFileSn;
		//첨부된 파일 정보 불러오기
		final Map<String, MultipartFile> files = request.getFileMap();
			//첨부된 파일이 존재할 경우
			if(!files.isEmpty()){
				//실제 저장소에 파일을 등록하고 FileVO 리턴
					_result = parseFileInf(files, "OSL_", inputFileSn, inputAtchFileId,"",addStrPath);
			}
		return _result;
	}
	
	/**
	 * 첨부파일에 대한 목록 정보를 취득한다.
	 *
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public List<FileVO> parseFileInf(Map<String, MultipartFile> files, String KeyStr, int fileKeyParam, String atchFileId, String storePath, String addStrPath) throws Exception {
		int fileKey = fileKeyParam;
		String storePathString = "";
		String atchFileIdString = "";

		if ("".equals(storePath) || storePath == null) {
			storePathString = EgovProperties.getProperty("Globals.fileStorePath");
		} else {
			storePathString = EgovProperties.getProperty(storePath);
		}
		//storePath 폴더 분류
		if(!"".equals(addStrPath) && addStrPath != null){
			storePathString += addStrPath+"/";
		}
		
		if ("".equals(atchFileId) || atchFileId == null) {
			atchFileIdString = idgenService.getNextStringId();
		} else {
			atchFileIdString = atchFileId;
		}

		File saveFolder = new File(EgovWebUtil.filePathBlackList(storePathString));

		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}

		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;
		String filePath = "";
		List<FileVO> result = new ArrayList<FileVO>();
		FileVO fvo;

		//파일 업로드 사이즈 구하기
		long fileInfoMaxSize = Long.parseLong(EgovProperties.getProperty("Globals.oslits.fileInfoMaxSize"));
		long fileSumMaxSize = Long.parseLong(EgovProperties.getProperty("Globals.oslits.fileSumMaxSize"));
		
		long fileSumSize = 0;
		while (itr.hasNext()) {
			
			Entry<String, MultipartFile> entry = itr.next();

			file = entry.getValue();

			String orginFileName = file.getOriginalFilename();
			//--------------------------------------
			// 원 파일명이 없는 경우 처리
			// (첨부가 되지 않은 input file type)
			//--------------------------------------
			
			if ("".equals(orginFileName)) {
				//클립보드판 ie 데이터 파일명이 ""이여서 제거 (2017-06-15)
				//continue;
				orginFileName = "image.png";
			}
			////------------------------------------

			int index = orginFileName.lastIndexOf(".");
			//String fileName = orginFileName.substring(0, index);
			String fileExt = orginFileName.substring(index + 1);
			
			//확장자가 없는 경우
			if(index == -1){
				continue;
			}
			
			/*
			 * 이미지 확장자 체크
			 * 저장 폴더 명(addStrPath)을 UsrImg로 지정 할 경우 확장자 체크 처리
			 */
			if("UsrImg".equals(addStrPath)){
				//확장자 이미지인지 체크
				String imgPattern = "(jpg|jpeg|png|gif)$";
				Pattern p = Pattern.compile(imgPattern);
				Matcher m = p.matcher(fileExt);
				
				//확장자 체크, 이미지 확장자가 아닐 경우
				if(m.find() == false){
					continue;
				}
			}
			
			
			String newName = KeyStr + getTimeStamp() + fileKey;
			long size = file.getSize();

			//파일 사이즈 체크하기
			if(size > fileInfoMaxSize || fileSumSize > fileSumMaxSize){
				continue;
			}
			
			fileSumSize += size;
			if (!"".equals(orginFileName)) {
				filePath = storePathString + File.separator + newName;
				file.transferTo(new File(EgovWebUtil.filePathBlackList(filePath)));
			}
			
			fvo = new FileVO();
			fvo.setFileExtsn(fileExt);
			fvo.setFileStreCours(storePathString);
			fvo.setFileMg(Long.toString(size));
			fvo.setOrignlFileNm(orginFileName);
			fvo.setStreFileNm(newName);
			fvo.setAtchFileId(atchFileIdString);
			fvo.setFileSn(String.valueOf(fileKey));

			result.add(fvo);

			fileKey++;
		}

		return result;
	}

	/**
	 * 첨부파일을 서버에 저장한다.
	 *
	 * @param file
	 * @param newName
	 * @param stordFilePath
	 * @throws Exception
	 */
	protected void writeUploadedFile(MultipartFile file, String newName, String stordFilePath) throws Exception {
		InputStream stream = null;
		OutputStream bos = null;

		try {
			stream = file.getInputStream();
			File cFile = new File(stordFilePath);

			if (!cFile.isDirectory()) {
				boolean _flag = cFile.mkdir();
				if (!_flag) {
					throw new IOException("Directory creation Failed ");
				}
			}

			bos = new FileOutputStream(stordFilePath + File.separator + newName);

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} finally {
			EgovResourceCloseHelper.close(bos, stream);
		}
	}

	/**
	 * 서버의 파일을 다운로드한다.
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void downFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String downFileName = "";
		String orgFileName = "";

		if ((String) request.getAttribute("downFile") == null) {
			downFileName = "";
		} else {
			downFileName = (String) request.getAttribute("downFile");
		}

		if ((String) request.getAttribute("orgFileName") == null) {
			orgFileName = "";
		} else {
			orgFileName = (String) request.getAttribute("orginFile");
		}

		orgFileName = orgFileName.replaceAll("\r", "").replaceAll("\n", "");

		File file = new File(EgovWebUtil.filePathBlackList(downFileName));

		if (!file.exists()) {
			throw new FileNotFoundException(downFileName);
		}

		if (!file.isFile()) {
			throw new FileNotFoundException(downFileName);
		}

		byte[] buffer = new byte[BUFF_SIZE]; //buffer size 2K.

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition:", "attachment; filename=" + new String(orgFileName.getBytes(), "UTF-8"));
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");

		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(new FileInputStream(file));
			outs = new BufferedOutputStream(response.getOutputStream());
			int read = 0;

			while ((read = fin.read(buffer)) != -1) {
				outs.write(buffer, 0, read);
			}
		} finally {
			EgovResourceCloseHelper.close(outs, fin);
		}
	}

	/**
	 * 첨부로 등록된 파일을 서버에 업로드한다.
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, String> uploadFile(MultipartFile file) throws Exception {

		HashMap<String, String> map = new HashMap<String, String>();
		//Write File 이후 Move File????
		String newName = "";
		String stordFilePath = EgovProperties.getProperty("Globals.fileStorePath");
		String orginFileName = file.getOriginalFilename();

		int index = orginFileName.lastIndexOf(".");
		//String fileName = orginFileName.substring(0, _index);
		String fileExt = orginFileName.substring(index + 1);
		long size = file.getSize();

		//newName 은 Naming Convention에 의해서 생성
		newName = getTimeStamp(); // 2012.11 KISA 보안조치
		writeFile(file, newName, stordFilePath);
		//storedFilePath는 지정
		map.put(Globals.ORIGIN_FILE_NM, orginFileName);
		map.put(Globals.UPLOAD_FILE_NM, newName);
		map.put(Globals.FILE_EXT, fileExt);
		map.put(Globals.FILE_PATH, stordFilePath);
		map.put(Globals.FILE_SIZE, String.valueOf(size));

		return map;
	}

	/**
	 * 파일을 실제 물리적인 경로에 생성한다.
	 *
	 * @param file
	 * @param newName
	 * @param stordFilePath
	 * @throws Exception
	 */
	protected static void writeFile(MultipartFile file, String newName, String stordFilePath) throws Exception {
		InputStream stream = null;
		OutputStream bos = null;

		try {
			stream = file.getInputStream();
			File cFile = new File(EgovWebUtil.filePathBlackList(stordFilePath));

			if (!cFile.isDirectory())
				cFile.mkdir();

			bos = new FileOutputStream(EgovWebUtil.filePathBlackList(stordFilePath + File.separator + newName));

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} finally {
			EgovResourceCloseHelper.close(bos, stream);
		}
	}

	/**
	 * 서버 파일에 대하여 다운로드를 처리한다.
	 *
	 * @param response
	 * @param streFileNm 파일저장 경로가 포함된 형태
	 * @param orignFileNm
	 * @throws Exception
	 */
	public void downFile(HttpServletResponse response, String streFileNm, String orignFileNm) throws Exception {
		String downFileName = streFileNm;
		String orgFileName = orignFileNm;

		File file = new File(downFileName);
		
		if (!file.exists()) {
			throw new FileNotFoundException(downFileName);
		}

		if (!file.isFile()) {
			throw new FileNotFoundException(downFileName);
		}

		int fSize = (int) file.length();
		if (fSize > 0) {
			BufferedInputStream in = null;

			try {
				in = new BufferedInputStream(new FileInputStream(file));

				String mimetype = "application/x-msdownload";

				//response.setBufferSize(fSize);
				response.setContentType(mimetype);
				response.setHeader("Content-Disposition:", "attachment; filename=" + orgFileName);
				response.setContentLength(fSize);
				//response.setHeader("Content-Transfer-Encoding","binary");
				//response.setHeader("Pragma","no-cache");
				//response.setHeader("Expires","0");
				FileCopyUtils.copy(in, response.getOutputStream());
			} finally {
				EgovResourceCloseHelper.close(in);
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}

		/*
		String uploadPath = propertiesService.getString("fileDir");

		File uFile = new File(uploadPath, requestedFile);
		int fSize = (int) uFile.length();

		if (fSize > 0) {
		    BufferedInputStream in = new BufferedInputStream(new FileInputStream(uFile));

		    String mimetype = "text/html";

		    //response.setBufferSize(fSize);
		    response.setContentType(mimetype);
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + requestedFile + "\"");
		    response.setContentLength(fSize);

		    FileCopyUtils.copy(in, response.getOutputStream());
		    in.close();
		    response.getOutputStream().flush();
		    response.getOutputStream().close();
		} else {
		    response.setContentType("text/html");
		    PrintWriter printwriter = response.getWriter();
		    printwriter.println("<html>");
		    printwriter.println("<br><br><br><h2>Could not get file name:<br>" + requestedFile + "</h2>");
		    printwriter.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
		    printwriter.println("<br><br><br>&copy; webAccess");
		    printwriter.println("</html>");
		    printwriter.flush();
		    printwriter.close();
		}
		//*/

		/*
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition:", "attachment; filename=" + new String(orgFileName.getBytes(),"UTF-8" ));
		response.setHeader("Content-Transfer-Encoding","binary");
		response.setHeader("Pragma","no-cache");
		response.setHeader("Expires","0");

		BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
		int read = 0;

		while ((read = fin.read(b)) != -1) {
		    outs.write(b,0,read);
		}
		log.debug(this.getClass().getName()+" BufferedOutputStream Write Complete!!! ");

		outs.close();
		fin.close();
		//*/
	}

	/**
	 * 공통 컴포넌트 utl.fcc 패키지와 Dependency제거를 위해 내부 메서드로 추가 정의함
	 * 응용어플리케이션에서 고유값을 사용하기 위해 시스템에서17자리의TIMESTAMP값을 구하는 기능
	 *
	 * @param
	 * @return Timestamp 값
	 * @see
	 */
	private static String getTimeStamp() {

		String rtnStr = null;

		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		String pattern = "yyyyMMddhhmmssSSS";

		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		rtnStr = sdfCurrent.format(ts.getTime());

		return rtnStr;
	}
	/**
	 * <pre>
	 * Comment : 파일을 삭제한다.
	 * </pre>
	 *
	 * @param fileDeletePath 삭제하고자 하는파일의 절대경로
	 * @return 성공하면 삭제된 파일의 절대경로, 아니면블랭크
	 */

	public static String deleteFile(String fileDeletePath) {

		// 인자값 유효하지 않은 경우 블랭크 리턴
		if (fileDeletePath == null || fileDeletePath.equals("")) {
			return "";
		}
		String result = "";
		File file = new File(EgovWebUtil.filePathBlackList(fileDeletePath));
		if (file.isFile()) {
			result = deletePath(fileDeletePath);
		} else {
			result = "";
		}
		return result;
	}
	
	/**
	 * <pre>
	 * Comment : 디렉토리(파일)를 삭제한다. (파일,디렉토리 구분없이 존재하는 경우 무조건 삭제한다)
	 * </pre>
	 *
	 * @param filePathToBeDeleted 삭제하고자 하는 파일의 절대경로 + 파일명
	 * @return 성공하면 삭제된 절대경로, 아니면블랭크
	 */

	public static String deletePath(String filePath) {
		File file = new File(EgovWebUtil.filePathBlackList(filePath));
		String result = "";
		if (file.exists()) {
			result = file.getAbsolutePath();
			if (!file.delete()) {
				result = "";
			}
		}
		return result;
	}
}
