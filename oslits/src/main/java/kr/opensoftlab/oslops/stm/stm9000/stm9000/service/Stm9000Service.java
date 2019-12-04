package kr.opensoftlab.oslops.stm.stm9000.stm9000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.stm.stm1000.stm1000.vo.Stm1000VO;
import kr.opensoftlab.oslops.stm.stm9000.stm9000.vo.Stm9000VO;



/**
 * @Class Name : Stm9000Service.java
 * @Description : Stm9000Service Business class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.17.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm9000Service {

	List<Stm9000VO> selectStm9000List(Stm9000VO stm9000vo);

	int selectStm9000ListCnt(Stm9000VO stm9000vo);

	Map selectStm9000DetailInfo(Map paramMap);

	List<Map> selectStm9000ReqHistoryList(Map paramMap);

	List<Map> selectStm9000ReqSignList(Map paramMap);

	List<Map> selectStm9000ChgDetailList(Map paramMap);
	

}
