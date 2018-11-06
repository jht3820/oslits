package kr.opensoftlab.oslits.stm.stm3000.stm3200.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.stm.stm3000.stm3200.vo.Stm3200VO;


/**
 * @Class Name : Stm3200Service.java
 * @Description : Stm3200Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm3200Service {

	List<Stm3200VO> selectStm3200JobProjectList(Stm3200VO stm3200vo);

	int selectStm3200JobProjectListCnt(Stm3200VO stm3200vo);

}
