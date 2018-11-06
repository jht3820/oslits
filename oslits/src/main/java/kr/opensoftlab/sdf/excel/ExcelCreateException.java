package kr.opensoftlab.sdf.excel;

/**
 * 엑셀파일 생성 시 에러가 났음을 나타낸다.
 * @author 조용상
 * @version 1.0
 * 
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
@SuppressWarnings("serial")
public class ExcelCreateException extends RuntimeException {
    public ExcelCreateException(String message) {
        super(message);
    }

    public ExcelCreateException(String message, Throwable exception) {
        super(message, exception);
    }
}