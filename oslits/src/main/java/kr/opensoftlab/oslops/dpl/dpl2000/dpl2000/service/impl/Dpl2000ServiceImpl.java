package kr.opensoftlab.oslops.dpl.dpl2000.dpl2000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.arm.arm1000.arm1000.service.impl.Arm1000DAO;
import kr.opensoftlab.oslops.dpl.dpl2000.dpl2000.service.Dpl2000Service;
import kr.opensoftlab.oslops.dpl.dpl2000.dpl2000.vo.Dpl2000VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Dpl1000ServiceImpl.java
 * @Description : Dpl1000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  2019-03-07		진주영		 	기능 개선
 *  
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("dpl2000Service")
public class Dpl2000ServiceImpl  extends EgovAbstractServiceImpl implements Dpl2000Service{
    
	/** Dpl2000DAO DI */
    @Resource(name="dpl2000DAO")
    private Dpl2000DAO dpl2000DAO;
    
    /**
	 * Dpl2000 배포계획 결재 목록 가져오기 
	 * @param param - Map
	 * @return list 배포자 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl2000SignList(Dpl2000VO dpl2000vo)  throws Exception{
		return dpl2000DAO.selectDpl2000SignList(dpl2000vo);
	}
	
	/**
	 * Dpl2000 배포계획 결재 목록 총건수
	 * @param Dpl2100VO - dpl1000VO
	 * @return 
	 * @exception Exception
	 */
	public int selectDpl2000SignListCnt(Dpl2000VO dpl2000vo) throws Exception {
		return dpl2000DAO.selectDpl2000SignListCnt(dpl2000vo);
	}
	
	/**
	 * Dpl2000 배포계획 결재 디테일 정보
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public Map selectDpl2000SignInfoAjax(Map map)  throws Exception{
		return dpl2000DAO.selectDpl2000SignInfoAjax(map);
	} 
}
