package kr.opensoftlab.oslops.req.req4700.req4700.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.req.req4700.req4700.service.Req4700Service;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


@Service("req4700Service")
public class Req4700ServiceImpl  extends EgovAbstractServiceImpl implements Req4700Service{

    @Resource(name="req4700DAO")
    private Req4700DAO req4700DAO;

    /**
	 * Req4700 요구사항 변경 내용 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public String insertReq4700ReqChangeLogInfo(Map paramMap) throws Exception{
		return req4700DAO.insertReq4700ReqChangeLogInfo(paramMap);
	}

}
