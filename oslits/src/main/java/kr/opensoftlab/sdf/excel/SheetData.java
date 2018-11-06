package kr.opensoftlab.sdf.excel;

import java.util.List;

/**
 * Excel Sheet의 Sheet 이름, Header, Record 데이터들을 표현한다.
 *
 * @author 조용상
 * @version 1.0
 * 
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
public class SheetData {
    private String sheetName;
    private SheetHeader sheetHeader;
    private List<SheetRecord> sheetRecords;

    /**
     * excel sheet 이름을 "Sheet1" 기본 값으로 대체한다.
     */
    public SheetData() {
        this("Sheet1");
    }

    /**
     * @param sheetName excel sheet 이름
     */
    public SheetData(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * @param sheetName excel sheet 이름
     * @param sheetHeader excel sheet header 정보
     * @param sheetRecords excel sheet record 정보
     */
    public SheetData(String sheetName, SheetHeader sheetHeader, List<SheetRecord> sheetRecords) {
        this.sheetName = sheetName;
        this.sheetHeader = sheetHeader;
        this.sheetRecords = sheetRecords;
    }

    /**
     * @return excel sheet 이름
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * @param name excel sheet 이름
     */
    public void setSheetName(String name) {
        this.sheetName = name;
    }

    /**
     * @return excel sheet header 정보
     */
    public SheetHeader getSheetHeader() {
        return sheetHeader;
    }

    /**
     * @return excel sheet record 정보
     */
    public List<SheetRecord> getSheetRecords() {
        return sheetRecords;
    }

    /**
     * @param sheetHeader excel sheet header 정보
     */
    public void setSheetHeader(SheetHeader sheetHeader) {
        this.sheetHeader = sheetHeader;
    }

    /**
     * @param sheetRecords excel sheet recored 정보
     */
    public void setSheetRecords(List<SheetRecord> sheetRecords) {
        this.sheetRecords = sheetRecords;
    }
}