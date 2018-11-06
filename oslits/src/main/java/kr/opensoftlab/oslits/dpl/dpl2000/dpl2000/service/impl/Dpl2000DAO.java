package kr.opensoftlab.oslits.dpl.dpl2000.dpl2000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.dpl.dpl2000.dpl2000.vo.Dpl2000VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Dpl2000DAO.java
 * @Description : Dpl2000DAO DAO Class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("dpl2000DAO")
public class Dpl2000DAO extends ComOslitsAbstractDAO {
	/**
	 * Dpl2000  배포버전별 요구사항 확인 SELECT BOX 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDpl2000SelectBox(Map paramMap) throws Exception {
		 return (List) list("dpl2000DAO.selectDpl2000SelectBox", paramMap);
    }

	/**
	 * Dpl2000 배포 버전 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDpl2000dplVerList(Map paramMap) throws Exception {
		return (List) list("dpl2000DAO.selectDpl2000dplVerList", paramMap);
	}
	
	
	
	/**
	 * 배포버전별 요구사항 확인 목록을 조회한다.
	 * @param Dpl2000VO
	 * @return List 배포버전별 요구사항 확인 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Dpl2000VO> selectDpl2000List(Dpl2000VO dpl2000VO) throws Exception {
		 return (List<Dpl2000VO>) list("dpl2000DAO.selectDpl2000List", dpl2000VO);
	}
	
	
	
	/**
	 * 배포버전별 요구사항 확인 목록 총건수를 조회한다.
	 * @param Dpl2000VO
	 * @return  int 배포버전별 요구사항 확인 목록 총건수 
	 * @throws Exception
	 */
	public int selectDpl2000ListCnt(Dpl2000VO dpl2000VO) throws Exception {
		 return (Integer) select("dpl2000DAO.selectDpl2000ListCnt", dpl2000VO);
	}
	
	
	
	/**
	 * 배포버전별 요구사항 확인 디테일 단건정보 조회
	 * @param Map
	 * @return Map 배포버전별 요구사항 확인 디테일 단건정보
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectDpl2000DetailAjax(Map paramMap) throws Exception{
		return (Map) select("dpl2000DAO.selectDpl2000DetailAjax", paramMap);
	}
	
	
	
	/**
	 * 배포버전별 요구사항 확인 코멘트 목록 조회
	 * @param Map
	 * @return List 배포버전별 요구사항 확인 코멘트 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDpl2000CommentListAjax(Map paramMap) throws Exception{
		return (List) list("dpl2000DAO.selectDpl2000CommentListAjax", paramMap);
	}
	
	
	
	/**
	 * 배포버전별 요구사항 확인 코멘트 등록
	 * @param Map
	 * @return void 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertDpl2000ReqCommentInfoAjax(Map paramMap) throws Exception{
		return (String) insert("dpl2000DAO.insertDpl2000ReqCommentInfoAjax", paramMap);
	}
	
	
	
	/**
	 * 배포버전별 요구사항 확인 코멘트 목록 조회
	 * @param Map
	 * @return List 배포버전별 요구사항 확인 코멘트 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDpl2000ReqCommentListAjax(Map paramMap) throws Exception{
		return (List) list("dpl2000DAO.selectDpl2000ReqCommentListAjax", paramMap);
	}
	
	/**
	 * 배포 버전별 요구사항 확인  Job별 빌드결과 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map> selectDpl2000JobBuildList(Map paramMap)  throws Exception{
		return (List) list("dpl2000DAO.selectDpl2000JobBuildList", paramMap);
	}
	
	/**
	 * 빌드이력이 업데이트 되지 않는 목록 조회 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List selectDpl2000BuildStatus(Map paramMap)  throws Exception{
		return (List) list("dpl2000DAO.selectDpl2000BuildStatus", paramMap);
	}
	/**
	 * 
	 * 배포별 job 이력의 빌드 상태를 업데이트 
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateDpl2000BuildStatus(Map paramMap) throws Exception {
		return update("dpl2000DAO.updateDpl2000BuildStatus", paramMap);
	}
	

}
