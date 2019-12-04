package kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.vo.Dpl1000VO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.vo.Dpl1300VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.jenkins.vo.BuildVO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Dpl1000DAO.java
 * @Description : Dpl1000DAO DAO Class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  2019-03-07		진주영		 	기능 개선
 *  
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("dpl1000DAO")
public class Dpl1000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Dpl1000 배포자 리스트 조회
	 * @param param - Map
	 * @return list 배포자 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl1000DeployNmList(Map inputMap)  throws Exception{
		return (List) list("dpl1000DAO.selectDpl1000DeployNmList", inputMap);
	}
	
	/**
	 * Dpl1000배포 계획 정보 일반 목록(No Page) 가져오기
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl1000DeployVerNormalList(Map inputMap)  throws Exception{
		return (List) list("dpl1000DAO.selectDpl1000DeployVerNormalList", inputMap);
	} 
	
	/**
	 * Dpl1000 배포 계획 리스트 조회
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked" })
	public List<Dpl1000VO> selectDpl1000DeployVerInfoList(Dpl1000VO dpl1000VO)  throws Exception{
		return (List<Dpl1000VO>) list("dpl1000DAO.selectDpl1000DeployVerInfoList", dpl1000VO);
	} 
	
	/**
	 * Dpl1000 배포 계획 단일 정보 조회
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public Map selectDpl1000DeployVerInfo(Map map)  throws Exception{
		return (Map) select("dpl1000DAO.selectDpl1000DeployVerInfo", map);
	} 
	
	/**
	 * Dpl1300 배포 계획에 배정된 JOB 목록
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl1300DeployJobList(Map inputMap)  throws Exception{
		return (List) list("dpl1000DAO.selectDpl1300DeployJobList", inputMap);
	}
	
	/**
	 * Dpl1300배포 계획 배정 JOB 목록 가져오기 (그리드)
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public List selectDpl1300dplJobGridList(Dpl1300VO dpl1300VO)  throws Exception{
		return (List) list("dpl1000DAO.selectDpl1300dplJobGridList", dpl1300VO);
	} 
	
	/**
	 * Dpl1300 배포 계획 배정 JOB 목록 리스트 총건수
	 * @return 
	 * @exception Exception
	 */
	public int selectDpl1300dplJobGridListCnt(Dpl1300VO dpl1300VO) throws Exception {
		return (Integer) select("dpl1000DAO.selectDpl1300dplJobGridListCnt", dpl1300VO);
	}
	
	/**
	 * Dpl1000 배포 계획별 요구사항 카운트
	 * @param param - Map
	 * @return Map 배포 계획별 요구사항수
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl1000ReqCount(Map inputMap)  throws Exception{
		return (List) list("dpl1000DAO.selectDpl1000ReqCount", inputMap);
	}
	
	/**
	 * Dpl1000 배포 계획 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertDpl1000DeployVerInfo(Map paramMap) throws Exception{
		return (String) insert("dpl1000DAO.insertDpl1000DeployVerInfo", paramMap);
    }
	
	/**
	 * Dpl1001 배포 JOB 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertDpl1300DeployJobInfo(Map paramMap) throws Exception{
		insert("dpl1000DAO.insertDpl1300DeployJobInfo", paramMap);
	}
	
	/**
	 * Dpl1000 배포 계획 수정
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateDpl1000DeployVerInfo(Map paramMap) throws Exception{
		update("dpl1000DAO.updateDpl1000DeployVerInfo", paramMap);
	}
	
	/**
	 * Dpl1000 배포 계획 배포 상태 수정
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateDpl1000DplStsCdInfo(Map paramMap) throws Exception{
		update("dpl1000DAO.updateDpl1000DplStsCdInfo", paramMap);
	}
	
	/**
	 * Dpl1000 배포 계획 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteDpl1000DeployVerInfo(Map paramMap) throws Exception{
		update("dpl1000DAO.deleteDpl1000DeployVerInfo", paramMap);
    }
	
	/**
	 * Dpl1000 배포 계획 리스트 총 건수
	 * @param Dpl2000VO - dpl1000VO
	 * @return 
	 * @exception Exception
	 */
	public int selectDpl1000ListCnt(Dpl1000VO dpl1000vo) throws Exception {
		return (Integer) select("dpl1000DAO.selectDpl1000ListCnt", dpl1000vo);
	}
	
	/**
	 * 요구사항 목록을 엑셀 다운로드용으로 조회한다.
	 * @param req2000VO
	 * @return List 로그인 이력 로그 목록
	 * @throws Exception
	 */
	public void  selectDpl1000ExcelList(Dpl1000VO dpl1000vo, ExcelDataListResultHandler resultHandler) throws Exception {
		listExcelDownSql("dpl1000DAO.selectDpl1000ExcelList", dpl1000vo, resultHandler);
	}

	@SuppressWarnings({"unchecked" })
	public List<Dpl1000VO> selectDpl1000BuildInfoList(Dpl1000VO dpl1000VO)  throws Exception{
		return (List<Dpl1000VO>) list("dpl1000DAO.selectDpl1000BuildInfoList", dpl1000VO);
	} 

	public int selectDpl1000BuildInfoListCnt(Dpl1000VO dpl1000vo) throws Exception {
		return (Integer) select("dpl1000DAO.selectDpl1000BuildInfoListCnt", dpl1000vo);
	}
	
	/**
	 * Dpl1400 배포 계획에 배정된 JOB에 해당하는 배포 실행 이력 조회 
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public Map selectDpl1400DplJobBuildInfo(Map map)  throws Exception{
		return (Map) select("dpl1000DAO.selectDpl1400DplJobBuildInfo", map);
	}
	
	/**
	 * Dpl1400 배포 계획에 배정된 JOB에 해당하는 배포 실행 이력 단건 조회 
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public Map selectDpl1400DplSelBuildInfoAjax(Map map)  throws Exception{
		return (Map) select("dpl1000DAO.selectDpl1400DplSelBuildInfoAjax", map);
	}
	
	/**
	 * Dpl1300 배포계획에 등록된 Job 삭제 
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteDpl1300DplJobList(Map paramMap)  throws Exception{
		 delete("dpl1000DAO.deleteDpl1300DplJobList", paramMap);
	}
	
	/**
	 * Dpl1400 배포 계획 실행 이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	public void insertDpl1400DeployJobBuildLogInfo(BuildVO buildVo) throws Exception{
		insert("dpl1000DAO.insertDpl1400DeployJobBuildLogInfo", buildVo);
    }
	
	/**
	 * Dpl1000 배포 계획 실행,수정,결재 이력
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl1000DplHistoryList(Map inputMap)  throws Exception{
		return (List) list("dpl1000DAO.selectDpl1000DplHistoryList", inputMap);
	}
	
	/**
	 * Dpl1000 Job 빌드 목록 조회
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl1400DplBldNumList(Map inputMap)  throws Exception{
		return (List) list("dpl1000DAO.selectDpl1400DplBldNumList", inputMap);
	}
	
	/**
	 * Dpl1500 배포계획 수정이력 CHG_ID 구하기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public String selectDpl1500NewChgId(Map paramMap) throws Exception{
		return (String)select("dpl1000DAO.selectDpl1500NewChgId", paramMap);
	}
	
	/**
	 * Dpl1500 배포계획 수정이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public String insertDpl1500ModifyHistoryInfo(Map paramMap) throws Exception{
		return (String)insert("dpl1000DAO.insertDpl1500ModifyHistoryInfo", paramMap);
	}
	
	/**
	 * Dpl1500 배포계획 수정이력 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public List selectDpl1500ModifyHistoryList(Map paramMap) throws Exception{
		return (List) list("dpl1000DAO.selectDpl1500ModifyHistoryList", paramMap);
	}
	
	/**
	 * Dpl1000 모든 배포계획 자동배포 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public List selectDpl1000AllDplList(Map paramMap) throws Exception{
		return (List) list("dpl1000DAO.selectDpl1000AllDplList", paramMap);
	}
	
}
