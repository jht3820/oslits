package kr.opensoftlab.oslops.adm.adm5000.adm5100.service.impl;


import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm5000.adm5000.vo.Adm5000VO;
import kr.opensoftlab.oslops.adm.adm5000.adm5100.service.Adm5100Service;
import kr.opensoftlab.oslops.adm.adm5000.adm5100.vo.Adm5100VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("adm5100Service")
public class Adm5100ServiceImpl extends EgovAbstractServiceImpl implements Adm5100Service {
	
	/** DAO Bean Injection */
    @Resource(name="adm5100DAO")
    private Adm5100DAO adm5100DAO;  

    
	/**
	 *  시스템 사용 이력 로그 목록을 조회한다.
	 * @param adm5100VO
	 * @return List 로그인 이력 로그 목록
	 * @throws Exception
	 */
	public List<Adm5100VO> selectAdm5100List(Adm5100VO adm5100vo) throws Exception {
	       return adm5100DAO.selectAdm5100List(adm5100vo);
	}
	
	/**
	 *  시스템 사용 이력 로그 목록 총건수를 조회한다.
	 * @param adm5100VO
	 * @return  int 로그인 이력 로그 목록 총건수 
	 * @throws Exception
	 */
	public int selectAdm5100ListCnt(Adm5100VO adm5100vo) throws Exception {
		 return adm5100DAO.selectAdm5100ListCnt(adm5100vo);
	}

	@Override
	public void selectAdm5100ExcelList(Adm5100VO adm5100vo,
			ExcelDataListResultHandler resultHandler) throws Exception {
		// TODO Auto-generated method stub
		
		adm5100DAO.selectAdm5100ExcelList(adm5100vo,resultHandler);
	}
	//시스템 사용이력 쌓기
	public void insertAdm5100AuthLoginLog(Adm5100VO adm5100vo) throws Exception{
		adm5100DAO.insertAdm5100AuthLoginLog(adm5100vo);
	}
	
}
