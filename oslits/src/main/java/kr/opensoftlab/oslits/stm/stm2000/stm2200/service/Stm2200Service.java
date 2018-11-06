package kr.opensoftlab.oslits.stm.stm2000.stm2200.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.stm.stm2000.stm2200.vo.Stm2200VO;


/**
 * @Class Name : Stm2200Service.java
 * @Description : Stm2200Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm2200Service {


	List<Stm2200VO> selectStm2200RepProjectList(Stm2200VO stm2200vo);

	int selectStm2200RepProjectListCnt(Stm2200VO stm2200vo);

}
