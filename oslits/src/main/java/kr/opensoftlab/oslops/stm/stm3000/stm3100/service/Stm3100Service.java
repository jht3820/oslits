package kr.opensoftlab.oslits.stm.stm3000.stm3100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.stm.stm3000.stm3100.vo.Stm3100VO;


/**
 * @Class Name : Stm3100Service.java
 * @Description : Stm3100Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm3100Service {


	List selectStm3100JenkinsProjectAuthList(Map paramMap) throws Exception;

	void saveStm3100(List<Stm3100VO> list);

	List<Map> selectStm3100JenkinsProjectList(Map paramMap);

}
