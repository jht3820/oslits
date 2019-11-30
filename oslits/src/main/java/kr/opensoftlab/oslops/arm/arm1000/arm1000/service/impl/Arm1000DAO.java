package kr.opensoftlab.oslops.arm.arm1000.arm1000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.arm.arm1000.arm1000.vo.Arm1000VO;
import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Arm1000DAO.java
 * @Description : Arm1000DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.01.03.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Repository("arm1000DAO")
public class Arm1000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Arm1000 사용자 쪽지 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectArm1000AlarmList(Arm1000VO arm1000VO) throws Exception {
		 return (List) list("arm1000DAO.selectArm1000AlarmList", arm1000VO);
    }
	
	/**
	 * Arm1000 사용자 쪽지 갯수 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectArm1000AlarmCnt(Map<String, String> paramMap) throws Exception {
		return (Map) select("arm1000DAO.selectArm1000AlarmCnt", paramMap);
	}
	
	/**
	 * Arm1000 쪽지 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertArm1000AlarmInfo(Map paramMap) throws Exception{
		return (String) insert("arm1000DAO.insertArm1000AlarmInfo", paramMap);
	}
	
	/**
	 * Arm1000 쪽지 수정 (삭제 또는 읽음 처리)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateArm1000AlarmInfo(Map paramMap) throws Exception{
		update("arm1000DAO.updateArm1000AlarmInfo", paramMap);
	}
	
	/**
	 * Arm1000 사용자 쪽지 내용 불러오기(단건)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectArm1000AlarmInfo(Map paramMap) throws Exception {
		return (Map) select("arm1000DAO.selectArm1000AlarmInfo", paramMap);
	}
	
	/**
	 * Arm1000 쪽지 삭제 (사용자 제거)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateArm1000AlarmList(Map paramMap) throws Exception{
		update("arm1000DAO.updateArm1000AlarmList", paramMap);
	}
	
	/**
	 * Adm1300 해당 프로젝트 권한 목록 불러오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public List selectAdm1300AuthUsrList(Map paramMap) throws Exception {
		return (List) list("arm1000DAO.selectAdm1300AuthUsrList", paramMap);
	}
}
