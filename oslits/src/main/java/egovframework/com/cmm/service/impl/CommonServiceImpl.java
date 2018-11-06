package egovframework.com.cmm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




import egovframework.com.cmm.service.CommonService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * @Class Name : CommonServiceImpl.java
 * @Description : 
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------

 *
 * @author 공대영
 * @since 2018. 7. 24.
 * @version
 * @see
 *
 */
@Service("commonService")
public class CommonServiceImpl extends EgovAbstractServiceImpl implements CommonService {

    @Resource(name = "commonDAO")
    private CommonDAO commonDAO;

    public String selectServerTime(Map<String,String> paramMap) throws Exception {
    	return commonDAO.selectServerTime(paramMap);
    }

	@Override
	public List<Map> selectDynamicComboBoxAjax(Map<String, String> paramMap)
			throws Exception {
		// TODO Auto-generated method stub
		return commonDAO.selectDynamicComboBoxAjax(paramMap);
	}
       
}
