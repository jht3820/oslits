package kr.opensoftlab.oslops.req.req4600.req4600.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.req.req4600.req4600.vo.Req4600VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Req4600DAO.java
 * @Description : Req4600DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2017.06.07
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("req4600DAO")
public class Req4600DAO extends ComOslitsAbstractDAO {
	/**
	 * Req4600 요구사항 클립보드 내용 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public String insertReq4600CBPaste(Map paramMap) throws Exception{
		return (String)insert("req4600DAO.insertReq4600CBPaste", paramMap);
	}
	
	/**
	 * 요구사항 CB_ATCH_FILE_ID 등록
	 * @param 	param - Map
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updateReq4100CbAtchFileId(Map paramMap) throws Exception{
		return update("req4600DAO.updateReq4100CbAtchFileId", paramMap);		
	}
	
	/**
	 * 요구사항 클립보드 데이터 불러오기
	 * @param 	param - Map
	 * @return	List
	 * @throws 	Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map>  selectReq4600CBContentList(Map paramMap) throws Exception {
		 return  (List<Map>) list("req4600DAO.selectReq4600CBContentList", paramMap);
	}
	
	/**
	 * 요구사항 클립보드 데이터 FILE_SN값 구하기
	 * @return	List
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	public int  selectReq4600CBFileSnMax(Map paramMap) throws Exception {
		return  (int) select("req4600DAO.selectReq4600CBFileSnMax", paramMap);
	}
	/**
	 * 요구사항 클립보드 데이터 단건 조회
	 * @param 	param - Map
	 * @return	Map
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq4600CBContentInfo(Map paramMap) throws Exception {
		return  (Map) select("req4600DAO.selectReq4600CBContentInfo", paramMap);
	}
	
	/**
	 * 요구사항 클립보드 데이터 단건 삭제
	 * @param 	param - Map
	 * @return	void
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteReq4600CBContentInfo(Map paramMap) throws Exception {
		delete("req4600DAO.deleteReq4600CBContentInfo", paramMap);
	}
	
	/**
	 * 요구사항 클립보드 데이터 단건 수정
	 * @param 	param - Map
	 * @return	int
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updateReq4600CBContentInfo(Map paramMap) throws Exception {
		return (int)update("req4600DAO.updateReq4600CBContentInfo", paramMap);
	}
	
	/**
	 * 요구사항 클립보드 다음 SEQ 구하기
	 * @param 	param - Map
	 * @return	Map
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectReq4600CBMaxSeq(Map paramMap) throws Exception {
		return  (int) select("req4600DAO.selectReq4600CBMaxSeq", paramMap);
	}
	
	
	/**
	 * REQ4100 요구사항 WBS 항목 조회
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4600ReqWbsListAjax(Map paramMap) throws Exception {
		return (List) list("req4600DAO.selectReq4600ReqWbsListAjax",paramMap);
	}

	/**
	 * Req4600 요구사항 WBS 진척률 수정
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateReq4600ProgresInfo(Map paramMap) {
		update("req4600DAO.updateReq4600ProgresInfo", paramMap);
	}
}
