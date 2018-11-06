package kr.opensoftlab.sdf.excel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Excel Sheet의 Record 정보를 표현한다.
 *
 * @author 조용상
 * @version 1.0
 * 
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
public class SheetRecord {
    private List<Object> cellDatas = new LinkedList<Object>();

    /**
     * excel record 중 하나의 cell 값을 추가한다.
     * @param data 추가할 cell 값
     */
    public void addCellData(Object data) {
        cellDatas.add(data);
    }

    /**
     * excel record 중 하나의 data에 해당하는 cell 값을 삭제한다.
     * @param data 삭제할 cell 값
     */
    public void removeCellData(Object data) {
        cellDatas.remove(data);
    }

    /**
     * excel record의 cell 값을 record 단위로 추가한다.
     * @param cells record 단위의 cell 값
     */
    public void setCellDatas(List<Object> cells) {
        this.cellDatas = cells;
    }

    /**
     * excel record의 cell 값을 record 단위로 추가한다.
     * @param cells cells record 단위의 cell 값
     */
    public void setCellDatas(Object[] cells) {
        Collections.addAll(this.cellDatas, cells);
    }

    /**
     * @return excel record의 cell 값들
     */
    public List<Object> getCellDataSet() {
        return cellDatas;
    }

    /**
     * @return  excel record의 cell 값들
     */
    public Object[] getCellDataArrays() {
        return cellDatas.toArray(new Object[cellDatas.size()]);
    }
}