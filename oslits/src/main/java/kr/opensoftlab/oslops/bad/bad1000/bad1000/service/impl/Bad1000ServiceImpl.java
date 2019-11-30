package kr.opensoftlab.oslops.bad.bad1000.bad1000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.opensoftlab.oslops.bad.bad1000.bad1000.service.Bad1000Service;
import kr.opensoftlab.oslops.bad.bad1000.bad1000.vo.Bad1000VO;

/**
 * @Class Name : Bad1000ServiceImpl.java
 * @Description : Bad1000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 전예지
 * @since 2019.08.29
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */


@Service("bad1000Service")
public class Bad1000ServiceImpl extends EgovAbstractServiceImpl implements Bad1000Service {

	/**/
	@Resource(name="bad1000DAO")
	private Bad1000DAO bad1000DAO;
	
	/**
	 * Bad1000 공지사항 목록을 조회한다.
	 * @param Bad1000VO
	 * @return List - 공지사항 목록
	 * @exception Exception
	 */
	@Override
	public List<Bad1000VO> selectbad1000BoardList(Bad1000VO bad1000vo) throws Exception {
		return bad1000DAO.selectbad1000BoardList(bad1000vo);
	}

	/**
	 * Bad1000 공지사항 목록 총 수를 조회한다.
	 * @param Bad1000VO
	 * @return List - 공지사항 목록 수
	 * @exception Exception
	 */
	@Override
	public int selectbad1000BoardListCnt(Bad1000VO bad1000vo) throws Exception {
		return  bad1000DAO.selectbad1000BoardListCnt(bad1000vo);
	}

	/**
	 * Bad1000 공지사항 정보 단건을 조회한다.
	 * @param Bad1000VO
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map selectBad1001Info(Map<String, String> paramMap) throws Exception {
		
		Map<String, String> bad1001List = null;
		
		// 조회수 업데이트
		int result = bad1000DAO.updateBad1001CntInfo(paramMap);
		
		if(result > 0) {
			// 공지사항 정보 단건 조회
			bad1001List = bad1000DAO.selectBad1001Info(paramMap);
		}
	
		return bad1001List;
	}

	/**
	 * Bad1000 공지사항 정보 등록/수정한다.
	 * @param paramMap
	 * @return Map
	 * @exception Exception
	 */
	@Override
	public int saveBad1001Info(Map<String, String> paramMap) throws Exception {
		
		// 등록, 수정 구분값
		String type = (String) paramMap.get("popupGb");
		int updateRst = 0;
		String insertRst = null;
		int result = 0;
		
		//수정인경우 
		if("update".equals(type)){
			updateRst = bad1000DAO.updateBad1001Info(paramMap);
		}
		//등록인경우 
		else{
			insertRst = bad1000DAO.InsertBad1001Info(paramMap);
		}
		
		// 등록 또는 수정 성공 시
		if(updateRst > 0 || insertRst != null) {
			result = 1;
		}
		
		return result;
	}

	/**
	 * bad1000 공지사항 글을 삭제한다.
	 * @param paramMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int deleteBad1000Info(Map<String, Object> paramMap) throws Exception {
		
		List<Map<String,String>> badIdList = (List<Map<String,String>>) paramMap.get("list");
		int deleteCnt =0;
		// 공지사항 삭제
		for(Map newMap : badIdList) {
			// 제거
			deleteCnt = bad1000DAO.deleteBad1000Info(newMap);
			
		}
		return deleteCnt;
	}


}
