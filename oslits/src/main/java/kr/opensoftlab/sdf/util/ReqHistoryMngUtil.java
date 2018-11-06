package kr.opensoftlab.sdf.util;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.prj.prj1000.prj1100.service.Prj1100Service;
import kr.opensoftlab.oslits.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslits.req.req4000.req4200.service.Req4200Service;
import kr.opensoftlab.oslits.req.req4000.req4800.service.Req4800Service;



/**
 * 요구사항 정보의 변경정보 저장 유틸
 * select, insert, delete, update로 시작하는 페이지 이름만 이력 저장
 * @author 김현종
 * @since 2017.07.03
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 * 수정일      	   수정자          		수정내용
 * -------    --------    ---------------------------
 * 2018-08-29	진주영		기존 수정이력 제거 후 REQ4800 수정이력 적용
 *  </pre>		
 */


public class ReqHistoryMngUtil {
	
	/** 로그4j 로거 로딩 */
	//private static final Logger Log = Logger.getLogger(ReqHistoryMngUtil.class);
	
	/** Req4200Service DI */
	@Resource(name = "req4200Service")
	private Req4200Service req4200Service;
	
	/** Req4800Service DI */
	@Resource(name = "req4800Service")
	private Req4800Service req4800Service;
	
	/** Req4100Service DI */
	@Resource(name = "req4100Service")
	private Req4100Service req4100Service;
	
	/** Prj1100Service DI */
	@Resource(name = "prj1100Service")
	private Prj1100Service prj1100Service;
	
	/**
	 * 요구사항 정보의 저장 유형(요구사항 등록, 변경, 삭제)에 대한 처리 구분
	 * 수정 정보를 List<Map>으로 담아서 return 해준다.
	 * DB처리는 service내에서 처리할 것.
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> insertReqHistory(Map paramMap) throws Exception{
		List<Map> modifyList = null;
		
		//기존 요구사항 정보 불러오기
		Map reqInfoMap = (Map) req4100Service.selectReq4102ReqInfoAjax(paramMap);
		
		//추가 항목 정보 불러오기
		Map addOptMap = (Map)prj1100Service.selectFlw1200ReqOptList(paramMap);
		
		//변경된 정보들 등록
		req4800Service.insertReq4800ModifyHistoryList(paramMap);
		
		return modifyList;
	}

}
