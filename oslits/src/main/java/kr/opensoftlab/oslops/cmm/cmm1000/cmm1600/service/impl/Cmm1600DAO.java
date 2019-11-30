package kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.service.impl;

import java.util.List;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.vo.Cmm1600VO;
import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Cmm1600DAO.java
 * @Description : Cmm1600DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("cmm1600DAO")
public class Cmm1600DAO extends ComOslitsAbstractDAO {
	
	
	/**
	 * Cmm1600 배포 계획 목록을 조회 한다.(공통)
	 * @param cmm1600VO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public List<Cmm1600VO> selectCmm1600CommonDplList(Cmm1600VO cmm1600VO) throws Exception {
		return (List) list("cmm1600DAO.selectCmm1600CommonDplList", cmm1600VO);
	}
	
	/**
	 * Cmm1600 배포 계획 목록의 총 건수를 조회한다. (그리드 페이징 처리)
	 * @param Cmm1600VO
	 * @return
	 * @throws Exception
	 */
	public int selectCmm1600CommonDplListCnt(Cmm1600VO cmm1600VO) throws Exception {
		return  (Integer)select("cmm1600DAO.selectCmm1600CommonDplListCnt", cmm1600VO);
	}

}
