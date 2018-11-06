package kr.opensoftlab.sdf.excel;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.ResultContext;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.ibatis.sqlmap.client.event.RowHandler;



/**
 * 대용량 엑셀을 처리하기 위해 Mybatis의 ResultHandler를 구현한 구현체
 * 건건이 XMLSheetWriter를 이용하여 데이터를 쓴다.
 *
 * @author Jason Park
 * @version 1.0
 * 
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * 2016-02-13    공대영 	해당사용 프레임워크가 mybatis의 ResultHandler를 지원하지 않아
 * 						iBatis의 RowHandler 핸들러를 대체 구현
 * 
 * </pre>
 */
public class ExcelDataListResultHandler implements RowHandler  {

	@SuppressWarnings("unused")
    private int iTotRowCount = 0;
	private int rowNum = 0;
	private XMLSheetWriter sw;
	private Map<String, XSSFCellStyle> styles;
    private SheetHeader sheetHeader;
    private List<Metadata> metadatas;
    
	public ExcelDataListResultHandler(XMLSheetWriter sw, Map<String, XSSFCellStyle> styles, 
	        SheetHeader sheetHeader, List<Metadata> metadatas){
		this.sw = sw;
		this.styles = styles;
		this.sheetHeader = sheetHeader;
		this.metadatas = metadatas;
		
		createSheetHeaders();
	}
	
    /**
     * Mybatis ResultHandler의 handleResult 구현
     * addRow 호출
     * 
     * @param resultContext
     */

    public void handleResult(ResultContext resultContext) {
	    Map row = (Map) resultContext.getResultObject();
        addRow(row);
    }
	
    @Override
	public void handleRow(Object valueObject){
    	//Map row = (Map) resultContext.getResultObject();
    	
        addRow((Map)valueObject);
	}
	
    /**
     * 엑셀의 Header를 구성한다.
     * 
     */
	private void createSheetHeaders() {
        int i = 0;
        try {
            //Cell Merge
//            if(this.arrCellMerg != null)
//                this.sw.mergeCell(this.arrCellMerg);
            
            CellStyle style = this.styles.get("header");
            this.sw.insertRow(this.rowNum);
            this.sw.createCell(i++, "No.", style.getIndex());
            Set<String> headers = this.sheetHeader.getHeaderSet();
            for (String header : headers) {
                this.sw.createCell(i++, header, style.getIndex());
            }
            this.sw.endRow();
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        this.iTotRowCount++;
        this.rowNum ++;
    }
    
    /**
     * Metadata에서 정보를 가져와서 Row의 각 Cell단위로 데이터를 기록한다.
     * 
     * @param dataMap
     */
    private void addRow(Map dataMap) throws RuntimeException {
        int i = 0;
        try {
            this.sw.insertRow(this.rowNum);
            this.sw.createCell(i++, this.rowNum);
            Object data = "";
            short style = 0;
            for (Metadata metadata : metadatas) {
                data = dataMap.get(metadata.getName());
                style = this.styles.get(metadata.getName()).getIndex();
                if (data instanceof Date) {
                    this.sw.createCell(i++, (Date)data, style);
                } else if (data instanceof Boolean) {
                    this.sw.createCell(i++, (String)data, style);
                } else if (data instanceof BigDecimal) {
                    this.sw.createCell(i++, Double.parseDouble(data.toString()), style);
                } else if (data instanceof String) {
                    /**
                     * DB컬럼은 String형이지만 실제 내용은 Number인 경우 Format이 적용되지 않거나 
                     * "텍스트로 표시된 숫자" 오류 방지(CHAR형 날짜 등 처리)
                     * 0으로 시작하면 Text로 출력함.(-> "텍스트로 표시된 숫자")
                     */
                    if(isNumber(data.toString()) && !data.toString().startsWith("0"))
                        this.sw.createCell(i++, Double.parseDouble(data.toString()), style);
                    else
                        this.sw.createCell(i++, (String)data, style);
                } else if (data != null){
                    this.sw.createCell(i++, data.toString(), style);
                } else {
                    this.sw.createCell(i++, "", style);
                }
            }
            this.sw.endRow();
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        this.iTotRowCount++;
        this.rowNum ++;
    }
    
    /**
     * 숫자인지 검사
     * 
     * @param str
     */
    public  boolean isNumber(String str){
        boolean result = false; 
        try{
            Double.parseDouble(str) ;
            result = true ;
        }catch(Exception e){}
        return result ;
    }


}
