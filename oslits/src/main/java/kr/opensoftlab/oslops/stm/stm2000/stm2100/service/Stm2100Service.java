package kr.opensoftlab.oslits.stm.stm2000.stm2100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.stm.stm2000.stm2100.vo.Stm2100VO;



/**
 * @Class Name : Stm2100Service.java
 * @Description : Stm2100Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm2100Service {

	List selectStm2100ProjectListAjax(Map paramMap) throws Exception;

	void saveStm2100(List<Stm2100VO> list);

	List selectStm2100ProjectAuthList(Map paramMap) throws Exception;

}
