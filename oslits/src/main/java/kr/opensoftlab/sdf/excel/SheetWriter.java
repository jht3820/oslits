package kr.opensoftlab.sdf.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SheetHeader, SheetRecord 객체를 이용하여 Excel 파일을 만든다.
 * 기본적인 목록 형태만 지원하므로 Cell 머지(Merge), Font Style 등 복잡한 구조의
 * Excel 파일을 만들어야 할 경우 Apache POI를 사용하여 직접 구현하도록 한다.
 *
 * @author 조용상
 * @version 1.0
 * 
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
public abstract class SheetWriter {
    private final Logger logger = LoggerFactory.getLogger(SheetWriter.class);
    public static final Format DEFAULT_FILENAME = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA);
    public static final int DEFAULT_ROW_ACCESS_SIZE = 1000;

    private SheetData sheetData;
    private int sheetRecordCount = 0;
    
    /**
     * Excel 파일을 write 한다.
     * @param path 엑셀파일이 저자될 경로
     * @return write된 excel 파일
     * @throws ExcelCreateException excel 파일 write 실패의 경우
     */
    public File wrtie(String path) throws ExcelCreateException {
        return wrtie(path, -1);
    }
    
    /**
     * Excel 파일을 write 한다.
     * @param path 엑셀파일이 저자될 경로
     * @param rowAccessSize 엑셀 저장시 몇 건씩 저장을 할 것인지의 수
     * @return write된 excel 파일
     * @throws ExcelCreateException excel 파일 write 실패의 경우
     */
    public File wrtie(String path, int rowAccessSize) throws ExcelCreateException {
        sheetData = getSheetData();

        Workbook workbook = new SXSSFWorkbook(rowAccessSize > 0 ? rowAccessSize : DEFAULT_ROW_ACCESS_SIZE);
        Sheet sheet = workbook.createSheet(sheetData.getSheetName());

        wirteHeader(sheet, sheetData.getSheetHeader());
        wirteRecord(sheet, sheetData.getSheetRecords());

        for (int i = 0; i < sheetData.getSheetHeader().getHeaderSet().size(); i++) {
            sheet.autoSizeColumn(i);
        }

        File excelFile = null;
        FileOutputStream out = null;
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                FileUtils.forceMkdir(dir);
            }
            excelFile = new File(dir, DEFAULT_FILENAME.format(new Date()) + ".xls");
            out = new FileOutputStream(excelFile);
            workbook.write(out);
        } catch (IOException e) {
            throw new ExcelCreateException(e.getMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                    ((FileOutputStream) workbook).close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return excelFile;
    }

    private void wirteHeader(Sheet sheet, SheetHeader sheetHeader) {
        if (sheetHeader != null) {
            Set<String> headers = sheetHeader.getHeaderSet();
            Row row = sheet.createRow(sheetRecordCount++);
            int cellCount = 0;

            for (String header : headers) {
                Cell cell = row.createCell(cellCount++);
                cell.setCellValue(header);
            }
        }
    }

    private void wirteRecord(Sheet sheet, List<SheetRecord> sheetRecords) {
        if (sheetRecords != null) {
            DataFormat dataFormat= sheet.getWorkbook().createDataFormat();
            CellStyle dateStyle = sheet.getWorkbook().createCellStyle();
            dateStyle.setDataFormat(dataFormat.getFormat(getDateFormatStyle()));

            int cellCount = 0;

            for (SheetRecord record : sheetRecords) {
                Row row = sheet.createRow(sheetRecordCount++);
                List<Object> cellDatas = record.getCellDataSet();

                for (Object cellData : cellDatas) {
                    Cell cell = row.createCell(cellCount++);

                    if (cellData instanceof Date) {
                        cell.setCellStyle(dateStyle);
                        cell.setCellValue((Date)cellData);
                    } else if (cellData instanceof Boolean) {
                        cell.setCellValue((Boolean)cellData);
                    } else if (cellData instanceof Double) {
                        cell.setCellValue((Double)cellData);
                    } else if (cellData instanceof String) {
                        cell.setCellValue((String)cellData);
                    } else if (cellData != null){
                        cell.setCellValue(cellData.toString());
                    } else {
                    	cell.setCellValue("");
                    }
                }
                cellCount = 0;
            }
        }
    }

    /**
     * Excel 파일 생성을 위해 excel sheet 데이터를 반환한다.
     * @return excel sheet 데이터
     */
    public abstract SheetData getSheetData();

    /**
     * Excel 날짜형식의 포멧을 반환한다.
     * @return 날짜형식의 포멧
     */
    public abstract String getDateFormatStyle();
}