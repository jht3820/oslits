package kr.opensoftlab.oslops.arm.arm1000.arm1000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.arm.arm1000.arm1000.vo.Arm1000VO;

/**
 * @Class Name : Arm1000Service.java
 * @Description : Arm1000Service Business class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.01.03.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Arm1000Service {
	
	/**
	 * Arm1000 사용자 쪽지 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectArm1000AlarmList(Arm1000VO arm1000VO) throws Exception;
	
	/**
	 * Arm1000 사용자 쪽지 갯수 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectArm1000AlarmCnt(Map<String, String> paramMap) throws Exception;
	/**
	 * Arm1000 쪽지 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String insertArm1000AlarmInfo(Map paramMap) throws Exception;
	
	/**
	 * Arm1000 쪽지 수정 (삭제 또는 읽음 처리)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	void updateArm1000AlarmInfo(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * Arm1000 사용자 쪽지 내용 불러오기(단건)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectArm1000AlarmInfo(Map paramMap) throws Exception;
	
	/**
	 * Arm1000 쪽지 삭제 (사용자 제거)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateArm1000AlarmList(Map paramMap) throws Exception;
	
	/**
	 * Adm1300 해당 프로젝트 권한 목록 불러오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	List selectAdm1300AuthUsrList(Map paramMap) throws Exception;
}
