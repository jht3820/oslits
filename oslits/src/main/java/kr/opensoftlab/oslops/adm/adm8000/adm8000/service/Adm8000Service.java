package kr.opensoftlab.oslits.adm.adm8000.adm8000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.stm.stm3000.stm3000.vo.Stm3000VO;

import com.sun.star.uno.Exception;

/**
 * @Class Name : Adm8000Service.java
 * @Description : Adm8000Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.09.03
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

public interface Adm8000Service {

	Object saveAdm8000MasterInfo(Map<String, String> paramMap);

	List<Map> selectAdm8000MasterYearList(Map<String, String> paramMap);

	List<Map> selectAdm8000MasterList(Map<String, String> paramMap);

	Map selectAdm8000MasterInfo(Map map) throws Exception;

	void deleteAdm8000MasterInfo(Map<String, String> paramMap);

	Object saveAdm8000DetailInfo(Map<String, String> paramMap);

	List<Map> selectAdm8000DetailList(Map<String, String> paramMap);

	Map selectAdm8000DetailInfo(Map<String, String> paramMap);

	int deleteAdm8000DetailInfo(Map<String, String> paramMap);

	Object saveAdm8000CopyInfo(Map<String, String> paramMap);

}
