package kr.opensoftlab.sdf.ux.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.opensoftlab.sdf.ux.service.UxPageMoveService;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("uxPageMoveService")
public class UxPageMoveServiceImpl extends EgovAbstractServiceImpl implements UxPageMoveService {

    @SuppressWarnings("rawtypes")
	public List selectUxPageMoveList(Map param) throws Exception {
    	return new ArrayList(); 
    }
    
}
