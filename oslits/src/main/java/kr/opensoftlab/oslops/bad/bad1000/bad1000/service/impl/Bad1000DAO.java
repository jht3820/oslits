package kr.opensoftlab.oslops.bad.bad1000.bad1000.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.opensoftlab.oslops.bad.bad1000.bad1000.vo.Bad1000VO;
import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

/**
 * @Class Name : Bad1000DAO.java
 * @Description : Bad1000DAO DAO Class
 * @Modification Information
 *
 * @author 전예지
 * @since 2019.08.29
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("bad1000DAO")
public class Bad1000DAO extends ComOslitsAbstractDAO {

	/**
	 * Bad1000 공지사항 목록을 조회한다.
	 * @param Bad1000VO
	 * @return List - 공지사항 목록
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Bad1000VO> selectbad1000BoardList(Bad1000VO bad1000vo) throws Exception {
		return (List) list("bad1000DAO.selectbad1000BoardList", bad1000vo);
	}

	/**
	 * Bad1000 공지사항 목록 총 수를 조회한다.
	 * @param Bad1000VO
	 * @return List - 공지사항 목록 수
	 * @exception Exception
	 */
	public int selectbad1000BoardListCnt(Bad1000VO bad1000vo) throws Exception {
		return (Integer) select("bad1000DAO.selectbad1000BoardListCnt", bad1000vo);
	}

	/**
	 * Bad1000 공지사항 정보 단건을 조회한다.
	 * @param Bad1000VO
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectBad1001Info(Map<String, String> paramMap) throws Exception {
		return (Map) select("bad1000DAO.selectBad1001Info", paramMap);
	}

	/**
	 * Bad1000 조회수를 업데이트한다.
	 * @param Map
	 * @return Map
	 * @exception Exception
	 */
	public int updateBad1001CntInfo(Map<String, String> paramMap) throws Exception {
		return (int) update("bad1000DAO.updateBad1001CntInfo", paramMap);
	}


	/**
	 * Bad1001 공지사항 글을 수정한다.
	 * @param Map
	 * @return Map
	 * @exception Exception
	 */
	public int updateBad1001Info(Map<String, String> paramMap) throws Exception {
		return update("bad1000DAO.updateBad1001Info", paramMap);
	}

	/**
	 * Bad1001 공지사항 글을 등록한다.
	 * @param Map
	 * @return Map
	 * @exception Exception
	 */
	public String InsertBad1001Info(Map<String, String> paramMap) throws Exception {
		return (String) insert("bad1000DAO.InsertBad1001Info", paramMap);
	}

	/**
	 * bad1000 공지사항 글을 삭제한다.
	 * @param paramMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int deleteBad1000Info(Map<String, Object> paramMap) throws Exception {
		return delete("bad1000DAO.deleteBad1000Info", paramMap);
	}

}
