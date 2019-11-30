package kr.opensoftlab.oslops.com.dao;

import javax.annotation.Resource;

import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapSession;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * ComEgmisAbstractDAO.java 클래스
 * 
 * @author 정형택
 * @since 2013. 11. 06.
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일     		 		수정자            수정내용
 *  ---------------    -------------   ------------------------------
 *   2013. 11. 06.   		정형택        	최초 생성
 * </pre>
 */

@Repository("comEgmisAbstractDAO")
public class ComOslitsAbstractDAO extends EgovAbstractDAO{

	@Resource(name="oslops.sqlMapClient")
	public void setSuperSqlMapClient(SqlMapClient sqlMapClient) {
        super.setSuperSqlMapClient(sqlMapClient);
    }
	
	/**
	 * 	공통 대용량 엑셀 다운로드 메서드(소용량도 포함)
	 * 	쿼리 ID와 파라미터 오브젝트(Map or Bean), 엑셀리절트핸들러를 이용해 엑셀 다운로드용 쿼리를 실행한다. 
	 * 	@param sqlId
	 * 	@param param
	 * 	@param resultHandler
	 * 	@throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void listExcelDownSql(String sqlId, Object param, ExcelDataListResultHandler resultHandler) throws Exception{
		SqlMapSession sqlMap = getSqlMapClient().openSession();
		sqlMap.queryWithRowHandler(sqlId, param, resultHandler);
		sqlMap.close();
	}
}

