package kr.opensoftlab.oslops.req.req2000.req2100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;

import org.springframework.stereotype.Repository;

@Repository("req2100DAO")
public class Req2100DAO  extends ComOslitsAbstractDAO {

	
	/**
	 * Req2100 프로세스 별 요구사항 처리완료 건수 조회
	 * @param Map
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked","rawtypes"})
	List<Map> selectReq2100ProcessReqEndCntAjax(Map paramMap) throws Exception{
		return (List<Map>) list("req2100DAO.selectReq2100ProcessReqEndCntAjax", paramMap);
	}


}