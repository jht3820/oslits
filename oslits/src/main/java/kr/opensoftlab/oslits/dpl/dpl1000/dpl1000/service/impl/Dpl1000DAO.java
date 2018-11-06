package kr.opensoftlab.oslits.dpl.dpl1000.dpl1000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.dpl.dpl1000.dpl1000.vo.Dpl1000VO;
import kr.opensoftlab.oslits.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

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
	 * Dpl1000 배포 버전 리스트 조회
	 * @param param - Map
	 * @return list 배포 버전 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked" })
	public List<Dpl1000VO> selectDpl1000DeployVerInfoList(Dpl1000VO dpl1000VO)  throws Exception{
		return (List<Dpl1000VO>) list("dpl1000DAO.selectDpl1000DeployVerInfoList", dpl1000VO);
	} 
	
	/**
	 * Dpl1000 배포 버전 단일 정보 조회
	 * @param param - Map
	 * @return list 배포 버전 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked" })
	public Map selectDpl1000DeployVerInfo(Map map)  throws Exception{
		return (Map) select("dpl1000DAO.selectDpl1000DeployVerInfo", map);
	} 
	
	/**
	 * Dpl1000 배포 버전별 요구사항 카운트
	 * @param param - Map
	 * @return Map 배포 버전별 요구사항수
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl1000ReqCount(Map inputMap)  throws Exception{
		return (List) list("dpl1000DAO.selectDpl1000ReqCount", inputMap);
	}
	
	/**
	 * Dpl1000 배포 버전 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertDpl1000DeployVerInfo(Map paramMap) throws Exception{
		insert("dpl1000DAO.insertDpl1000DeployVerInfo", paramMap);
    }
	
	/**
	 * Dpl1000 배포 버전 수정
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateDpl1000DeployVerInfo(Map paramMap) throws Exception{
		update("dpl1000DAO.updateDpl1000DeployVerInfo", paramMap);
	}
	
	/**
	 * Dpl1000 배포 버전 리스트 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteDpl1000DeployVerInfoList(Map paramMap) throws Exception{
		insert("dpl1000DAO.deleteDpl1000DeployVerInfoList", paramMap);
    }
	
	/**
	 * Dpl1000 배포 버전 리스트 총 건수
	 * @param Dpl1000VO - dpl1000VO
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
	
	public Map selectDpl1000BuildDetail(Map paramMap)  throws Exception{
		return (Map) select("dpl1000DAO.selectDpl1000BuildDetail", paramMap);
	}
	
}
