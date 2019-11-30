package kr.opensoftlab.oslops.whk.whk2000.whk2000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.opensoftlab.oslops.whk.whk2000.whk2000.service.Whk2000Service;
import kr.opensoftlab.oslops.whk.whk2000.whk2000.vo.Whk2000VO;

@Service("whk2000Service")
public class Whk2000ServiceImpl extends EgovAbstractServiceImpl implements Whk2000Service {
	
	/** DAO Bean Injection */
    @Resource(name="whk2000DAO")
    private Whk2000DAO whk2000DAO;
	


	/**
	 * 사용자 웹훅 목록 조회
	 * @param pageVO
	 * @return List 사용자 웹훅 목록
	 * @throws Exception
	 */
	public List<Whk2000VO>  selectWhk2000UsrWhkList(Whk2000VO whk2000vo) throws Exception {
		 return  whk2000DAO.selectWhk2000UsrWhkList(whk2000vo);
	}
	
	/**
	 * 사용자 웹훅 목록 총건수
	 * @param Whk2000VO
	 * @throws Exception
	 */
	public int  selectWhk2000UsrWhkListCnt(Whk2000VO whk2000vo) throws Exception {
		 return  whk2000DAO.selectWhk2000UsrWhkListCnt(whk2000vo);
	} 
	
	/**
	 * 사용자 웹훅 전체 목록 조회 (no paging)
	 * @param Map
	 * @return List 사용자 웹훅 목록
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<Map> selectWhk2000UsrWhkAllList(Map paramMap) throws Exception{
		return whk2000DAO.selectWhk2000UsrWhkAllList(paramMap);
	}
	
	/**
	 * 사용자 웹훅 단건 조회
	 * @param Map
	 * @return Map 사용자 웹훅 정보
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map selectWhk2000UsrWhkInfo(Map paramMap) throws Exception{
		return whk2000DAO.selectWhk2000UsrWhkInfo(paramMap);
	}
	
	/**
	 * 사용자 웹훅 정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertWhk2000UsrWhkInfo(Map paramMap) throws Exception{
		return whk2000DAO.insertWhk2000UsrWhkInfo(paramMap);
	}
	
	/**
	 * 사용자 웹훅 정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void updateWhk2000UsrWhkInfo(Map paramMap) throws Exception{
		whk2000DAO.updateWhk2000UsrWhkInfo(paramMap);
	}

	/**
	 * 사용자 웹훅 정보 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteWhk2000UsrWhkList(Map paramMap) throws Exception{
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		
		//prjId
		String prjId = (String)paramMap.get("prjId");
		
		//usrId
		String usrId = (String)paramMap.get("usrId");
		
		int listSize = list.size();
		
		for (int i = 0; i < listSize; i++) {
			Map<String,String> whkMap = list.get(i);
			whkMap.put("prjId", prjId);
			whkMap.put("usrId", usrId);
			
			//배포 계획 삭제
			whk2000DAO.deleteWhk2000UsrWhkInfo(whkMap);
		}
	}
}
