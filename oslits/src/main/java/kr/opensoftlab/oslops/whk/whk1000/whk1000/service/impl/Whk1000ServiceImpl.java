package kr.opensoftlab.oslops.whk.whk1000.whk1000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.opensoftlab.oslops.whk.whk1000.whk1000.service.Whk1000Service;
import kr.opensoftlab.oslops.whk.whk1000.whk1000.vo.Whk1000VO;

@Service("whk1000Service")
public class Whk1000ServiceImpl extends EgovAbstractServiceImpl implements Whk1000Service {
	
	/** DAO Bean Injection */
    @Resource(name="whk1000DAO")
    private Whk1000DAO whk1000DAO;
	

	/**
	 * 웹훅 목록 조회
	 * @return List 웹훅 목록
	 * @throws Exception
	 */
	public List<Whk1000VO>  selectWhk1000PrjWhkList(Whk1000VO whk1000vo) throws Exception {
		 return  (List<Whk1000VO>) whk1000DAO.selectWhk1000PrjWhkList(whk1000vo);
	}
	
	/**
	 * 웹훅 목록 총건수
	 * @throws Exception
	 */
	public int selectWhk1000PrjWhkListCnt(Whk1000VO whk1000vo) throws Exception {
		 return  whk1000DAO.selectWhk1000PrjWhkListCnt(whk1000vo);
	}
	
	/**
	 * 웹훅 전체 목록 조회 (no paging)
	 * @param Map
	 * @return Map 요구사항 정보
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectWhk1000PrjWhkAllList(Map paramMap) throws Exception{
		return whk1000DAO.selectWhk1000PrjWhkAllList(paramMap);
	}
	
	/**
	 * 웹훅 정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertWhk1000PrjWhkInfo(Map paramMap) throws Exception{
		return whk1000DAO.insertWhk1000PrjWhkInfo(paramMap);
	}

	/**
	 * 웹훅 단건 조회
	 * @param Map
	 * @return Map 웹훅 정보
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map selectWhk1000PrjWhkInfo(Map paramMap) throws Exception{
		return whk1000DAO.selectWhk1000PrjWhkInfo(paramMap);
	}
	
	/**
	 * 웹훅 정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void updateWhk1000PrjWhkInfo(Map paramMap) throws Exception{
		whk1000DAO.updateWhk1000PrjWhkInfo(paramMap);
	}
	
	/**
	 * 웹훅 정보 단건 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteWhk1000PrjWhkInfo(Map paramMap) throws Exception{
		return whk1000DAO.deleteWhk1000PrjWhkInfo(paramMap);
	}
	
	/**
	 * 웹훅 정보 목록 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteWhk1000PrjWhkList(Map paramMap) throws Exception{
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		
		//prjId
		String prjId = (String)paramMap.get("prjId");
		
		int listSize = list.size();
		
		for (int i = 0; i < listSize; i++) {
			Map<String,String> whkMap = list.get(i);
			whkMap.put("prjId", prjId);
			
			//배포 계획 삭제
			whk1000DAO.deleteWhk1000PrjWhkInfo(whkMap);
		}
	}
}
