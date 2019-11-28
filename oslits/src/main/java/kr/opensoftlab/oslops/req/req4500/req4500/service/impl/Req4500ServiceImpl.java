package kr.opensoftlab.oslops.req.req4500.req4500.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;





import kr.opensoftlab.oslops.req.req4500.req4500.service.Req4500Service;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Req4500ServiceImpl.java
 * @Description : Req4500ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 진주영
 * @since 2017.01.03.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Service("req4500Service")
public class Req4500ServiceImpl extends EgovAbstractServiceImpl implements Req4500Service {
	
	/** DAO Bean Injection */
    @Resource(name="req4500DAO")
    private Req4500DAO req4500DAO;
    
	@Override
	public List<Map> selectReq4500ReqHistoryList(Map<String, String> paramMap) {
		return req4500DAO.selectReq4500ReqHistoryList(paramMap);
	}
	
	@Override
	public List<Map> selectReq4500ReqSignList(Map<String, String> paramMap){
		return req4500DAO.selectReq4500ReqSignList(paramMap);
	}

	@Override
	public List selectReq4500ChgDetailList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return req4500DAO.selectReq4500ChgDetailList(paramMap);
	}
	
}
