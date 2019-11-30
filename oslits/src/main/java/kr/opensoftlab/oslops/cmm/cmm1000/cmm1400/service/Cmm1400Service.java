package kr.opensoftlab.oslops.cmm.cmm1000.cmm1400.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

/**
 * @Class Name : Adm4100Service.java
 * @Description : Adm4100Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

public interface Cmm1400Service {

	List<Map> selectCmm1400SVNRepositoryList(Map<String, String> paramMap);



}
