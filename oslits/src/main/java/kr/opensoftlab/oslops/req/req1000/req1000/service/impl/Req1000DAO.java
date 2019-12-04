package kr.opensoftlab.oslops.req.req1000.req1000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.req.req1000.req1000.vo.Req1000VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

@Repository("req1000DAO")
public class Req1000DAO  extends ComOslitsAbstractDAO {
	
	/**
	 * 요구사항 목록을 조회한다.
	 * @param req1000VO
	 * @return List 로그인 이력 로그 목록
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Req1000VO>  selectReq1000AllList(Req1000VO req1000vo) throws Exception {
		 return  (List<Req1000VO>) list("req1000DAO.selectReq1000AllList", req1000vo);
	}
	
	/**
	 * 요구사항 목록을 조회한다.
	 * @param req1000VO
	 * @return List 로그인 이력 로그 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Req1000VO>  selectReq1000List(Req1000VO req1000vo) throws Exception {
		return  (List<Req1000VO>) list("req1000DAO.selectReq1000List", req1000vo);
	}
	
	/**
	 * 요구사항 목록 총건수를 조회한다.
	 * @param req1000VO
	 * @return  int 로그인 이력 로그 목록 총건수 
	 * @throws Exception
	 */
	public int  selectReq1000ListCnt(Req1000VO req1000vo) throws Exception {
		 return  (Integer)select("req1000DAO.selectReq1000ListCnt", req1000vo);
	} 
	
	/**
	 * 요구사항 정보을 조회한다.
	 * @param Map
	 * @return Map 요구사항 정보
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq1000ReqInfo(Map paramMap) throws Exception{
		return (Map) select("req1000DAO.selectReq1000ReqInfo", paramMap);
	}
	
	/**
	 * Req1000 요구사항 정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertReq1001ReqInfo(Map paramMap) throws Exception{
		return (String) insert("req1000DAO.insertReq1001ReqInfo", paramMap);
	}
	
	/**
	 * Req1000 요구사항 key값 수정	 
 	 * 프로젝트 ID와 요구사항 등록 시 생성된 요구사항 ID를 조합하여 암호화 시킨 후
	 * 요구사항 키로 등록하여 요구사항의 고유한 해시값으로 사용한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void updateReq1000ReqKey(Map paramMap) throws Exception{
		update("req1000DAO.updateReq1000ReqKey",paramMap);
	}
	
	/**
	 * Req1000 요구사항 개발공수, 담당자 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void updateReq1001ReqSubInfo(Map paramMap) throws Exception{
		update("req1000DAO.updateReq1001ReqSubInfo",paramMap);
	}
	
	/**
	 * Req1000 요구사항 정보 수정
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updateReq1001ReqInfo(Map paramMap) throws Exception{
		return (int) update("req1000DAO.updateReq1001ReqInfo", paramMap);
	}
	
	/**
	 * Req1000 요구사항 정보 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteReq1001ReqInfo(Map paramMap) throws Exception{
		return (int) update("req1000DAO.deleteReq1001ReqInfo", paramMap);
	}

	public void selectReq1000ExcelList(Req1000VO req1000vo,
			ExcelDataListResultHandler resultHandler) throws Exception {
		listExcelDownSql("req1000DAO.selectReq1000ExcelList", req1000vo, resultHandler);
	}
	
	
	/**
	 * Req1000 요구사항 요청자 정보 조회 - 소속명, 이메일, 연락처
	 * @param  param - Map
	 * @return 요청자 정보
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq1000ReqUserInfo(Map paramMap) throws Exception {
		return	(Map) select("req1000DAO.selectReq1000ReqUserInfo", paramMap);
	}
	
	/**
	 * Req1000 현재 요구사항이 속한 프로젝트명, 프로젝트 약어 조회
	 * @param  param - Map
	 * @return 체계명
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq1000ReqPrjInfo(Map paramMap) throws Exception {
		return	(Map) select("req1000DAO.selectReq1000ReqPrjInfo", paramMap);
	}
	
	/**
	 * Req1000 요구사항 다음 순번정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq1000NextReqOrd(Map paramMap) throws Exception{
		return (Map) select("req1000DAO.selectReq1000NextReqOrd", paramMap);
	}	
	
	/**
	 * Req1000 요구사항 첨부파일 정보 삭제
	 * - 요구사항 삭제 시 해당 요구사항의 첨부파일 정보도 삭제한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteReq1000ReqAtchFile(Map paramMap) throws Exception{
		return (int) delete("req1000DAO.deleteReq1000ReqAtchFile", paramMap);
	}
	
	/**
	 * Req1000 요구사항 첨부파일 상세정보 삭제
	 * - 요구사항 삭제 시 해당 요구사항의 첨부파일 상세정보도 삭제한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteReq1000ReqAtchFileDetail(Map paramMap) throws Exception{
		return (int) delete("req1000DAO.deleteReq1000ReqAtchFileDetail", paramMap);
	}
	
	/**
	 * 톱합대시보드의 요구사항 접수대기 목록을 조회한다.
	 * @param paramMap
	 * @return List 접수대기 요구사항 목록
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<Map>  selectReq1000IntegratedDshAcceptReqList(Map paramMap) throws Exception {
		return  (List<Map>) list("req1000DAO.selectReq1000IntegratedDshAcceptReqList", paramMap);
	}
	
	/**
	 * 통합대시보드의 요구사항 접수대기 목록 총 건수를 조회한다.
	 * @param paramMap
	 * @return int 접수대기 요구사항 목록 총 건수
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int  selectReq1000IntegratedDshAcceptReqListCnt(Map paramMap) throws Exception {
		 return  (Integer)select("req1000DAO.selectReq1000IntegratedDshAcceptReqListCnt", paramMap);
	} 
	
}