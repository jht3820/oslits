package kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.vo.Dsh1000VO;

import org.springframework.stereotype.Repository;

@Repository("dsh1000DAO")
public class Dsh1000DAO  extends ComOslitsAbstractDAO {
	/**
	 * [차트1] 프로세스별 총갯수 + 최종 완료 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh1000ProcessReqCntList(Map paramMap) throws Exception{
		return (List) list("dsh1000DAO.selectDsh1000ProcessReqCntList", paramMap);
	}
	
	/**
	 * [차트2] 월별 프로세스별 요구사항 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh1000MonthProcessReqCntList(Map paramMap) throws Exception{
		return (List) list("dsh1000DAO.selectDsh1000MonthProcessReqCntList", paramMap);
	}

	public List selectDsh1000ReqList(Dsh1000VO dsh1000VO) throws Exception{
		return (List) list("dsh1000DAO.selectDsh1000ReqList", dsh1000VO);
	}

	public int selectDsh1000ReqListCnt(Dsh1000VO dsh1000VO) throws Exception{
		return (Integer) select("dsh1000DAO.selectDsh1000ReqListCnt", dsh1000VO);
	}
}