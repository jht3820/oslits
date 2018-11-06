package kr.opensoftlab.sdf.excel;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Excel Sheet의 헤더정보를 표현한다.
 *
 * @author 조용상
 * @version 1.0
 * 
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
public class SheetHeader {
    private Set<String> headers = new LinkedHashSet<String>();

    public SheetHeader() {
    }

    /**
     * @param sheetHeader excel header 값 들
     */
    public SheetHeader(String[] sheetHeader) {
        Collections.addAll(headers, sheetHeader);
    }

    /**
     * @param sheetHeader excel header 값 들
     */
    public SheetHeader(Set<String> sheetHeader) {
        this.headers = sheetHeader;
    }

    /**
     * @param label excel header 값을 설정한다.
     */
    public void addHeader(String label) {
        headers.add(label);
    }

    /**
     * @param label 삭제할 excel header 값
     */
    public void removeHeader(String label) {
        headers.remove(label);
    }

    /**
     * @param sheetHeader excel header 값 들
     */
    public void setSheetHeader(Set<String> sheetHeader) {
        this.headers = sheetHeader;
    }

    /**
     * @param sheetHeader  excel header 값 들
     */
    public void setSheetHeader(String[] sheetHeader) {
        Collections.addAll(headers, sheetHeader);
    }

    /**
     * @return  excel header set
     */
    public Set<String> getHeaderSet() {
        return headers;
    }

    /**
     * @return  excel header array
     */
    public String[] getHeaderArrays() {
        return headers.toArray(new String[headers.size()]);
    }
}