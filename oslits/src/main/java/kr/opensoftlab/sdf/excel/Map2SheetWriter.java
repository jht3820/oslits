package kr.opensoftlab.sdf.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * java.util.Map 을 기반으로로 하여 Map 데이터를 Excel 파일로 만든다.
 *
 * @author 조용상
 * @version 1.0
 * 
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
public class Map2SheetWriter extends SheetWriter {
    private final static String DEFAULT_DATE_FORMAT_STYLE = "yyyy-MM-dd";

    private SheetData sheetData;
    private SheetHeader sheetHeader;
    private List<Map> datas;
    private String[] keys = null;
    private String dateFormatStyle;

    /**
     * @param sheetHeader excel header 정보
     * @param datas excel 데이터
     * @param keys excel 데이터 중 어떤 정보를 사용할 것인지의 Map의 key 값 들
     */
    public Map2SheetWriter(SheetHeader sheetHeader, List<Map> datas, String[] keys) {
        this("Sheet1", sheetHeader, datas, keys, DEFAULT_DATE_FORMAT_STYLE);
    }

    /**
     * @param sheetName excel sheet 이름
     * @param sheetHeader excel header 정보
     * @param datas excel 데이터
     * @param keys excel 데이터 중 어떤 정보를 사용할 것인지의 Map의 key 값 들
     */
    public Map2SheetWriter(String sheetName, SheetHeader sheetHeader, List<Map> datas, String[] keys) {
        this(sheetName, sheetHeader, datas, keys, DEFAULT_DATE_FORMAT_STYLE);
    }

    /**
     * @param sheetName excel sheet 이름
     * @param sheetHeader excel header 정보
     * @param datas excel 데이터
     * @param keys excel 데이터 중 어떤 정보를 사용할 것인지의 Map의 key 값 들
     * @param dateFormatStyle 날짜형식의 포멧
     */
    public Map2SheetWriter(String sheetName, SheetHeader sheetHeader, List<Map> datas, String[] keys, String dateFormatStyle) {
        if (keys == null || keys.length == 0) {
            throw new IllegalArgumentException("The keys value can not be null.");
        }

        this.sheetHeader = sheetHeader;
        this.datas = datas;
        this.keys = keys;
        this.dateFormatStyle = dateFormatStyle;

        sheetData = new SheetData();
        sheetData.setSheetName(sheetName);
    }

    @Override
    public SheetData getSheetData() {
        List<SheetRecord> sheetRecords = new ArrayList<SheetRecord>();

        for (Map<String, Object> data : datas) {
            SheetRecord sheetRecord = new SheetRecord();

            for (String key : keys) {
                sheetRecord.addCellData(data.get(key));
            }
            sheetRecords.add(sheetRecord);
        }
        sheetData.setSheetHeader(sheetHeader);
        sheetData.setSheetRecords(sheetRecords);
        return sheetData;
    }

    @Override
    public String getDateFormatStyle() {
        return dateFormatStyle != null ? dateFormatStyle : DEFAULT_DATE_FORMAT_STYLE;
    }
}