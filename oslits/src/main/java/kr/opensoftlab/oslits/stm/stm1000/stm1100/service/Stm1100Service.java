package kr.opensoftlab.oslits.stm.stm1000.stm1100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.stm.stm1000.stm1000.vo.Stm1000VO;
import kr.opensoftlab.oslits.stm.stm1000.stm1100.vo.Stm1100VO;

/**
 * @Class Name : Api1100Service.java
 * @Description : Api1100Service Business class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.08.14.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm1100Service {

	List<Map> selectStm1100ProjectAuthList(Map<String, String> paramMap);

	List selectStm1100ProjectListAjax(Map<String, String> paramMap);

	void saveStm1100(List<Stm1100VO> list) throws Exception ;

	

}
