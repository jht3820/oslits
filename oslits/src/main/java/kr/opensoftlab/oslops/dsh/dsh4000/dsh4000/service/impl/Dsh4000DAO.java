package kr.opensoftlab.oslops.dsh.dsh4000.dsh4000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.dsh.dsh4000.dsh4000.vo.Dsh4000VO;

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
@Repository("dsh4000DAO")
public class Dsh4000DAO extends ComOslitsAbstractDAO {


	public List<Dsh4000VO> selectDsh4000ReportList(Dsh4000VO dsh4000VO) {
		// TODO Auto-generated method stub
		return (List) list("dsh4000DAO.selectDsh4000ReportList", dsh4000VO);
	}

	public int selectDsh4000ReportListCnt(Dsh4000VO dsh4000VO) {
		// TODO Auto-generated method stub
		return (Integer)select("dsh4000DAO.selectDsh4000ReportListCnt", dsh4000VO);
	}
		
}
