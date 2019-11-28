package kr.opensoftlab.oslops.com.fms.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.impl.EgovFileMngServiceImpl;
import egovframework.com.cmm.service.impl.FileManageDAO;

/**
 * @Class Name : FileMngServiceImpl.java
 * @Description : FileMngServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 진주영
 * @since 2016.02.15.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Service("fileMngService")
public class FileMngServiceImpl extends EgovFileMngServiceImpl implements FileMngService {
	
	@Resource(name = "FileManageDAO")
	private FileManageDAO fileMngDAO;
	/**
	 * 단건의 파일 마스터 ID 등록
	 *
	 * @param fileList
	 * @return
	 * @throws Exception
	 */
	public void insertFileMasterInfo(String atchFileId) throws Exception {
		fileMngDAO.insertFileMasterInfo(atchFileId);
	}
	

	/**
	 * 여러 개의 파일에 대한 정보(속성 및 상세)를 등록한다.
	 *
	 * @see egovframework.com.cmm.service.EgovFileMngService#insertFileInfs(java.util.List)
	 */
	public String insertFileInfs(List<?> fvoList) throws Exception {
		String atchFileId = "";

		if (fvoList.size() != 0) {
			atchFileId = fileMngDAO.insertFileInfs(fvoList);
		}
		if (atchFileId == "") {
			atchFileId = null;
		}
		return atchFileId;
	}
	/**
	 * 파일 리스트 조회
	 * @param atchFileId 파일고유ID
	 * @return List<FileVO>
	 * @throws Exception 
	 */
	public List<FileVO> fileDownList(FileVO fileVO) throws Exception{
		//파일 정보 담을 변수
   		List<FileVO> _result = null;

		_result = fileMngDAO.selectFileInfs(fileVO);

		return _result;
	}
	/**
	 * 파일명 검색에 대한 목록 전체 건수를 조회한다.
	 *
	 * @param fvo
	 * @return
	 * @throws Exception
	 */
	public int selectFileListCntByFileId(FileVO fvo) throws Exception {
		return fileMngDAO.selectFileListCntByFileId(fvo);
	}
	/**
     * 파일 구분자에 대한 최대값을 구한다.
     *
     * @param fvo
     * @return
     * @throws Exception
     */
    public int getFileSN(FileVO fvo) throws Exception{
    	return fileMngDAO.getMaxFileSN(fvo);
    }
}
