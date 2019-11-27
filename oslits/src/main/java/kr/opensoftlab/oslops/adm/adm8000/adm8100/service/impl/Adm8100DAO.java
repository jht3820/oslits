package kr.opensoftlab.oslits.adm.adm8000.adm8100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.adm.adm8000.adm8100.vo.Adm8100VO;
import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;

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
	

	@SuppressWarnings("rawtypes")
	public void insertAdm8100ReportInfo(Map paramMap) throws Exception{
		insert("adm8100DAO.insertAdm8100ReportInfo", paramMap);
	}

	public List<Adm8100VO> selectAdm8100ReportList(Adm8100VO adm8100vo) {
		// TODO Auto-generated method stub
		return (List) list("adm8100DAO.selectAdm8100ReportList", adm8100vo);
	}

	public int selectAdm8100ReportListCnt(Adm8100VO adm8100vo) {
		// TODO Auto-generated method stub
		return (Integer)select("adm8100DAO.selectAdm8100ReportListCnt", adm8100vo);
	}


	
	public List<Map> selectAdm8100ReportInfo(Map paramMap) {
		// TODO Auto-generated method stub
		return (List) list("adm8100DAO.selectAdm8100ReportInfo", paramMap);
	}

	public void updateAdm8100ReportInfo(Adm8100VO adm8100vo) {
		// TODO Auto-generated method stub
		update("adm8100DAO.updateAdm8100ReportInfo", adm8100vo);
	}

	public void updateAdm8100ReporConfirm(Adm8100VO adm8100vo) {
		// TODO Auto-generated method stub
		update("adm8100DAO.updateAdm8100ReporConfirm", adm8100vo);
	}

	public void insertAdm8100ReportMasterInfo(Map paramMap) {
		// TODO Auto-generated method stub
		insert("adm8100DAO.insertAdm8100ReportMasterInfo", paramMap);
	}

	public void updateAdm8100ReportWriteYn(Map paramMap) {
		// TODO Auto-generated method stub
		update("adm8100DAO.updateAdm8100ReportWriteYn", paramMap);
	}
	
	public Map selectAdm8100ReportMasterInfo(Map paramMap) {
		// TODO Auto-generated method stub
		return (Map)select("adm8100DAO.selectAdm8100ReportMasterInfo", paramMap);
	}
		
}
