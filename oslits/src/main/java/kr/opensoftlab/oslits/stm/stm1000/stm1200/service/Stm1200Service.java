package kr.opensoftlab.oslits.stm.stm1000.stm1200.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.stm.stm1000.stm1200.vo.Stm1200VO;



/**
 * @Class Name : Stm1000Service.java
 * @Description : Stm1000Service Business class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm1200Service {
	

	List<Stm1200VO> selectStm1200ProjectList(Stm1200VO stm1200vo);

	int selectStm1200ProjectListCnt(Stm1200VO stm1200vo);
	
}
