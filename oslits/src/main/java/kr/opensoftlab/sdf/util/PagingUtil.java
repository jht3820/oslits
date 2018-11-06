package kr.opensoftlab.sdf.util;

import kr.opensoftlab.oslits.com.vo.PageVO;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : PagingUtil.java
 * @Description : 페이징 처리를 위한 유틸 클래스
 * @Modification Information
 *
 * @author 안세웅
 * @since 2016.01.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

public class PagingUtil {
	
	/**
	 * PaginationInfo 객체를 생성하고, 페이징 관련 속성을 셋팅한다.
	 *   
	 * @param commonVO 페이징 처리를 위해 공통적으로 상속 받은 VO
	 * @return PaginationInfo 생성된 PaginationInfo 객체
	 */
	public static PaginationInfo getPaginationInfo(PageVO pageVO) {
	    PaginationInfo paginationInfo = new PaginationInfo();
	    paginationInfo.setCurrentPageNo(pageVO.getPageIndex());
	    paginationInfo.setRecordCountPerPage(pageVO.getPageUnit());
	    paginationInfo.setPageSize(pageVO.getPageSize());
	    
	    pageVO.setFirstIndex(paginationInfo.getFirstRecordIndex()); 
	    pageVO.setLastIndex(paginationInfo.getLastRecordIndex());
	    pageVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());	    
	     
		return paginationInfo;	
	}

	/**
	 * PaginationInfo 객체를 생성하고, 페이징 관련 속성을 셋팅한다.
	 * NoCountCommonVO인 경우에 사용한다.
	 *   
	 * @param commonVO 페이징 처리를 위해 공통적으로 상속 받은 VO
	 * @return PaginationInfo 생성된 PaginationInfo 객체
	 */
	/*
	public static PaginationInfo getPaginationInfo(NoCountCommonVO commonVO)
	{
	    PaginationInfo paginationInfo = new PaginationInfo();
	    paginationInfo.setCurrentPageNo(commonVO.getPageIndex());
	    paginationInfo.setRecordCountPerPage(commonVO.getPageUnit());
	    paginationInfo.setPageSize(commonVO.getPageSize());
	    
	    commonVO.setFirstIndex(paginationInfo.getFirstRecordIndex()); 
	    commonVO.setLastIndex(paginationInfo.getLastRecordIndex());
	    commonVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
	    
	    commonVO.setFirstIndexOnPageList( 
	    		(paginationInfo.getFirstPageNoOnPageList() - 1) * paginationInfo.getRecordCountPerPage() + 1
	    );
	    commonVO.setLastIndexOnPageList( 
	    		(paginationInfo.getFirstPageNoOnPageList() + commonVO.getPageSize() - 1) * paginationInfo.getRecordCountPerPage() 
	    );
	    
		return paginationInfo;	
	}*/
	
}