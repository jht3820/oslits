package kr.opensoftlab.sdf.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



import kr.opensoftlab.sdf.excel.annotation.ExcelColumn;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


/**
 * Annotation 기반의 Excel Parser.
 * 본 클래스는 목록형태의 단순한 엑셀 파싱을 손쉽게 파싱할 수 있도록 지원하는 클래스로
 * 복잡한 구조의 엑셀파일은 본 클래스에서 엑셀 파싱을 위해 사용하는 오픈소스인 POI를 활용하여
 * 직접 구현하도록 한다.
 *
 * @author 조용상
 * @version 1.0
 * 
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
public class SheetParser {
    private final Logger logger = Logger.getLogger(this.getClass());
    private final Workbook workbook;
    private final Sheet sheet;

    /**
     * 파싱할 엑셀파일을 File 객체로 받아 parser를 생성
     * sheet는 첫번째 sheet를 대상으로 한다.
     * @param file 엑셀파일
     * @throws InvalidFormatException 엑셀파일의 포멧이 잘 못된 경우
     * @throws IOException 엑셀파일이 없거나 읽는도중 에러가 날 경우
     */
    public SheetParser(File file) throws InvalidFormatException, IOException {
        this(file, 0);
    }

    /**
     * 파싱할 엑셀파일을 File 객체로 받아 parser를 생성
     * @param file 엑셀파일
     * @param sheetNo 파싱할  sheet 번호
     * @throws InvalidFormatException 엑셀파일의 포멧이 잘 못된 경우
     * @throws IOException 엑셀파일이 없거나 읽는도중 에러가 날 경우
     */
    public SheetParser(File file, int sheetNo) throws InvalidFormatException, IOException {
        this(new FileInputStream(file), sheetNo);
    }

    /**
     * 파싱할 엑셀파일의 Stream 객체로 받아 parser를 생성
     * sheet는 첫번째 sheet를 대상으로 한다.
     * @param inputStream 엑셀파일 Stream
     * @throws InvalidFormatException 엑셀파일의 포멧이 잘 못된 경우
     * @throws IOException 엑셀파일이 없거나 읽는도중 에러가 날 경우
     */
    public SheetParser(InputStream inputStream) throws InvalidFormatException, IOException {
        this(inputStream, 0);
    }

    /**
     * 파싱할 엑셀파일의 Stream 객체로 받아 parser를 생성
     * @param inputStream 엑셀파일 Stream
     * @param sheetNo 파싱할  sheet 번호
     * @throws InvalidFormatException 엑셀파일의 포멧이 잘 못된 경우
     * @throws IOException 엑셀파일이 없거나 읽는도중 에러가 날 경우
     */
    public SheetParser(InputStream inputStream, int sheetNo) throws InvalidFormatException, IOException {
        workbook = WorkbookFactory.create(inputStream);
        sheet = workbook.getSheetAt(sheetNo);
    }

    /**
     * 엑셀파일을 파싱하여 사용자 임의의 Class를 List 객체에 담아 리턴한다.
     * 엑셀파일 파싱은 보통 제목줄은 skip한 데이터를 담는 것이 일반적이므로 startRow 변수를 통해 어디서 부터
     * 파일을 파싱해야할지 정보를 주어야 한다. 엑셀의 로우는 배열과 같이 0부터 시작한다.
     * @param clazz 사용자 임의의 클래스
     * @param startRow 파싱 시작 로우
     * @return 사용자 임의의 클래스에 엑셀 파싱정보를 담은 List 객체
     * @throws ExcelParsingException 엑셀 파싱 중 에러가 났을 경우
     */
    public <T> List<T> createEntity(Class<T> clazz, int startRow) throws ExcelParsingException {
        List<T> list = new ArrayList<T>();
        Map<String, ExcelColumn> columnMap = getExcelColumnMap(clazz);

        if (!columnMap.isEmpty()) {
            T object = null;
            Row row = null;
            Cell cell = null;
            ExcelColumn excelColumn = null;
            int rowCount = sheet.getPhysicalNumberOfRows();
            
/*            for (int i = startRow; i < rowCount; i++) {
                object = getInstance(clazz);
                row = sheet.getRow(i);

                for (Map.Entry<String, ExcelColumn> entry : columnMap.entrySet()) {
                	//빈 행이 있는 경우 건너띄기
                	if(row == null){
                		break;
                	}
                    excelColumn = entry.getValue();
                    cell = row.getCell(excelColumn.position());
                    setProperty(object, entry.getKey(), cell, excelColumn);
                }
                list.add(object);
            }*/
            int i = startRow;
            
            //값이 있는 row 수
            int chkRow = 0;
            
            while(chkRow != (rowCount-1)){
            	object = getInstance(clazz);
                row = sheet.getRow(i);
                
                //10000행 까지 검사
            	if(i >= 10000){
            		break;
            	}
            	
            	
                //빈 행이 있는 경우 건너띄기
            	if(row == null){
            		i++;
            		continue;
            	}
            	
            	boolean cellValChk = false;
            	//컬럼 값 세팅
                for (Map.Entry<String, ExcelColumn> entry : columnMap.entrySet()) {
                    excelColumn = entry.getValue();
                    cell = row.getCell(excelColumn.position());
                    setProperty(object, entry.getKey(), cell, excelColumn);
                    
                    //cell 값 확인
                    if(convertCellStringValue(cell, excelColumn) != null && !"".equals(convertCellStringValue(cell, excelColumn))){
                    	cellValChk = true;
                    }
                	
                }
                
                //셀에 값이 한개도 없는경우 리스트 skip
                if(!cellValChk){
                	i++;
                	continue;
                }
                list.add(object);
                
                i++;
                chkRow++;
            }
        }
        return list;
    }

    /**
     * 사용자 임의의 클래스의 인스턴스를 얻어온다.
     * @param clazz 사용자 임의의 클래스
     * @return 사용자 임의의 클래스 인스턴스
     * @throws ExcelParsingException 인스터스 생성시 에러발생의 경우
     */
    private <T> T getInstance(Class<T> clazz) throws ExcelParsingException {
        T object = null;
        try {
            object = clazz.newInstance();
        } catch (Exception e) {
            logger.error(e);
            throw new ExcelParsingException("Exception occured while instantiating the class " + clazz.getName(), e);
        }
        return object;
    }

    /**
     * 사용자 임의의 클래스에 ExcelColumn annotation 정보를 바탕으로 특정 엑셀 Cell 값을 assign한다.
     * @param object 사용자 임의의 클래스 인스턴스
     * @param name Cell값을 assign할 메소드 명
     * @param cell 엑셀 Cell 정보
     * @param excelColumn 엑셀파일 중 특정 Cell 정보를 식별할 수 있는 annotation
     * @throws ExcelParsingException Cell정보를 assign하기 위해 리플렉션하는 과정에서 에러가 발생할 경우
     */
    private <T> void setProperty(T object, String name, Cell cell, ExcelColumn excelColumn)
            throws ExcelParsingException {
        try {
            if (excelColumn.dateFormat() != null) {
                DateTimeConverter dtConverter = new DateConverter();
                dtConverter.setPattern(excelColumn.dateFormat());
                ConvertUtils.register(dtConverter, Date.class);
            }
            BeanUtils.setProperty(object, name, convertCellStringValue(cell, excelColumn));
        } catch (Exception e) {
            logger.error(e);
            throw new ExcelParsingException("Exception occured while setting field value ", e);
        }
    }

    /**
     * 사용자 임의의 클래스내 Field(멤버변수)를 순회하여 ExcelColumn annotation과 field명을 찾아 Map 형태로 변환한다.
     * @param clazz 사용자 임의의 클래스
     * @return ExcelColumn annotation과 field명을 담은 Map객체
     */
    private <T> Map<String, ExcelColumn> getExcelColumnMap(Class<T> clazz) {
        Map<String, ExcelColumn> columnMap = new HashMap<String, ExcelColumn>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            if (excelColumn != null) {
                field.setAccessible(true);
                columnMap.put(field.getName(), excelColumn);
            }
        }
        return columnMap;
    }

    /**
     * 엑셀 Cell 값을 편히 처리하기 하기위해 엑셀의 모든 데이터 값을 String을 변환한다.
     * @param cell 엑셀 Cell정보
     * @param excelColumn Cell정보 중 데이터 포멧 변경 정보를 담은  ExcelColumn Annotation
     * @return 엑셀 Cell 값을 String으로 변환한 값
     */
    private String convertCellStringValue(Cell cell, ExcelColumn excelColumn) {
        if (cell == null) {
            return "";
        }
        String value = null;

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Format formatter = new SimpleDateFormat(excelColumn.dateFormat(), Locale.KOREA);
                    value = formatter.format(cell.getDateCellValue());
                } else {
                    value = (new BigDecimal(cell.getNumericCellValue())).toString();
                    if (value.endsWith(".0")) {
                        value = value.substring(0, value.length() - 2);
                    }
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                try {
                    value = (new BigDecimal(cell.getNumericCellValue())).toString();
                } catch (Exception e) {
                    try {
                        value = cell.getRichStringCellValue().getString();
                    } catch (Exception ex) {
                        value = cell.getCellFormula();
                    }
                }
                break;
            default:
                value = "";
                break;
        }
        return value;
    }
}