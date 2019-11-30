package kr.opensoftlab.oslops.adm.adm8000.adm8100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.adm.adm8000.adm8100.vo.Adm8100VO;
import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Adm8000DAO.java
 * @Description : 보고서 DAO 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.10.18  공대영          최초 생성
 *  
 * </pre>
 *  @author 공대영
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Repository("adm8100DAO")
public class Adm8100DAO extends ComOslitsAbstractDAO {
	

	/**
	 * Adm8100 보고서를 생성한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertAdm8100ReportInfo(Map paramMap) throws Exception{
		insert("adm8100DAO.insertAdm8100ReportInfo", paramMap);
	}

	/**
	 * Adm8100 보고서 목록을 조회한다.
	 * @param adm8100vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<Adm8100VO> selectAdm8100ReportList(Adm8100VO adm8100vo) throws Exception{
		return (List) list("adm8100DAO.selectAdm8100ReportList", adm8100vo);
	}

	/**
	 * Adm8100 보고서 목록 총 건수 조회
	 * @param adm8100vo
	 * @return
	 * @throws Exception
	 */
	public int selectAdm8100ReportListCnt(Adm8100VO adm8100vo) throws Exception{
		return (Integer)select("adm8100DAO.selectAdm8100ReportListCnt", adm8100vo);
	}

	/**
	 * Adm8100 보고서 단건 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<Map> selectAdm8100ReportInfo(Map paramMap) throws Exception{
		return (List) list("adm8100DAO.selectAdm8100ReportInfo", paramMap);
	}

	/**
	 * Adm8100 보고서를 수정한다.
	 * @param paramMap
	 * @param adm8100voList
	 * @return
	 * @throws Exception
	 */
	public void updateAdm8100ReportInfo(Adm8100VO adm8100vo) throws Exception{
		update("adm8100DAO.updateAdm8100ReportInfo", adm8100vo);
	}

	/**
	 * Adm8100 보고서를 확정 처리한다.
	 * @param adm8100vo
	 * @return
	 * @throws Exception
	 */
	public void updateAdm8100ReporConfirm(Adm8100VO adm8100vo) throws Exception{
		update("adm8100DAO.updateAdm8100ReporConfirm", adm8100vo);
	}

	/**
	 * Adm8100 보고서 마스터를 등록한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertAdm8100ReportMasterInfo(Map paramMap) throws Exception{
		insert("adm8100DAO.insertAdm8100ReportMasterInfo", paramMap);
	}

	/**
	 * Adm8100 보고서 작성자명을 수정한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateAdm8100ReportWriteYn(Map paramMap) throws Exception{
		update("adm8100DAO.updateAdm8100ReportWriteYn", paramMap);
	}
	
	/**
	 * Adm8100 보고서 마스터를 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm8100ReportMasterInfo(Map paramMap) throws Exception{
		return (Map)select("adm8100DAO.selectAdm8100ReportMasterInfo", paramMap);
	}
		
}
