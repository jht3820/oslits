package kr.opensoftlab.oslits.dpl.dpl2000.dpl2000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.service.impl.Dpl1100DAO;
import kr.opensoftlab.oslits.dpl.dpl2000.dpl2000.service.Dpl2000Service;
import kr.opensoftlab.oslits.dpl.dpl2000.dpl2000.vo.Dpl2000VO;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Dpl2000ServiceImpl.java
 * @Description : Dpl2000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("dpl2000Service")
public class Dpl2000ServiceImpl extends EgovAbstractServiceImpl implements Dpl2000Service{
	/** Dpl2000DAO DI */
    @Resource(name="dpl2000DAO")
    private Dpl2000DAO dpl2000DAO;

    @Resource(name="dpl1100DAO")
    private Dpl1100DAO dpl1100DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
    
    
	/**
   	 * Dpl2000 배포버전별 요구사항 확인 SELECT BOX 조회
   	 * @param param - Map
   	 * @return 
   	 * @exception Exception
   	 */
   	@SuppressWarnings("rawtypes")
   	public List selectDpl2000SelectBox(Map paramMap) throws Exception {
   		return dpl2000DAO.selectDpl2000SelectBox(paramMap) ;
   	}
   	
   	/**
   	 * Dpl2000 배포버전별 요구사항 확인 SELECT BOX 조회
   	 * @param param - Map
   	 * @return 
   	 * @exception Exception
   	 */
   	@SuppressWarnings("rawtypes")
   	public List selectDpl2000dplVerList(Map paramMap) throws Exception {
   		return dpl2000DAO.selectDpl2000dplVerList(paramMap);
   	}
   	
   	/**
	 * 배포버전별 요구사항 확인 목록을 조회한다.
	 * @param Dpl2000VO
	 * @return List 배포버전별 요구사항 확인 목록
	 * @throws Exception
	 */
	public List<Dpl2000VO> selectDpl2000List(Dpl2000VO dpl2000VO) throws Exception {
		return dpl2000DAO.selectDpl2000List(dpl2000VO);
	}
	
	
	
	/**
	 * 배포버전별 요구사항 확인 목록 총건수를 조회한다.
	 * @param Dpl2000VO
	 * @return  int 배포버전별 요구사항 확인 목록 총건수 
	 * @throws Exception
	 */
	public int selectDpl2000ListCnt(Dpl2000VO dpl2000VO) throws Exception {
		 return dpl2000DAO.selectDpl2000ListCnt(dpl2000VO);
	}
	
	
	
	/**
	 * 배포버전별 요구사항 확인 디테일 단건정보 조회
	 * @param Map
	 * @return Map 배포버전별 요구사항 확인 디테일 단건정보
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectDpl2000DetailAjax(Map paramMap) throws Exception{
		return dpl2000DAO.selectDpl2000DetailAjax(paramMap);
	}
	
	
	
	/**
	 * 배포버전별 요구사항 확 코멘트 목록 조회
	 * @param Map
	 * @return List 배포버전별 요구사항 확인 코멘트 목록
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List selectDpl2000CommentListAjax(Map paramMap) throws Exception{
		return dpl2000DAO.selectDpl2000CommentListAjax(paramMap);
	}
	
	
	
	/**
	 * 배포버전별 요구사항 확인 코멘트 등록
	 * @param Map
	 * @return void 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertDpl2000ReqCommentInfoAjax(Map paramMap) throws Exception{
		String reqCmntSeq = dpl2000DAO.insertDpl2000ReqCommentInfoAjax(paramMap);
		
		if( "".equals(EgovStringUtil.isNullToString(reqCmntSeq)) ){
			throw new Exception(egovMessageSource.getMessage("dpl2000.fail.cmmt.insert"));
		}
	}
	
	
	
	/**
	 * 배포버전별 요구사항 확인 코멘트 목록 조회
	 * @param Map
	 * @return List 배포버전별 요구사항 확인 코멘트 목록
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List selectDpl2000ReqCommentListAjax(Map paramMap) throws Exception{
		return dpl2000DAO.selectDpl2000ReqCommentListAjax(paramMap);
	}
	
	@Override
	public List<Map> selectDpl2000JobBuildList(Map paramMap) throws Exception {
		return dpl2000DAO.selectDpl2000JobBuildList(paramMap);
	}
	
	@Override
	public List selectDpl2000BuildStatus(Map paramMap) throws Exception {
		return dpl2000DAO.selectDpl2000BuildStatus(paramMap);
    }
	
	/**
	 * 배포별 job 이력에 결과를 업데이트
	 */
	@Override
	public int updateDpl2000BuildStatus(Map paramMap) throws Exception {
		return dpl2000DAO.updateDpl2000BuildStatus(paramMap);
	}
	
	@Override
	public void insertDpl1100logHistory(Map paramMap) throws Exception {
		// TODO Auto-generated method stub
		dpl1100DAO.insertDpl1100logHistory(paramMap);
		
	}
}
