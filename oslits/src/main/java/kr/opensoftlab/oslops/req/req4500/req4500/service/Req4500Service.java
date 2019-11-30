package kr.opensoftlab.oslops.req.req4500.req4500.service;

import java.util.List;
import java.util.Map;

/**
 * @Class Name : Req4500Service.java
 * @Description : Req4500Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.10.10.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Req4500Service {

	List<Map> selectReq4500ReqHistoryList(Map<String, String> paramMap);

	List<Map> selectReq4500ReqSignList(Map<String, String> paramMap);

	List selectReq4500ChgDetailList(Map<String, String> paramMap);
}
