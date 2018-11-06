package kr.opensoftlab.sdf.excel;

/**
 * 엑셀파일 파싱 수행 시 에러가 났음을 나타낸다.
 * @author 조용상
 * @version 1.0
 * 
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
@SuppressWarnings("serial")
public class ExcelParsingException extends RuntimeException {
    public ExcelParsingException(String message) {
        super(message);
    }

    public ExcelParsingException(String message, Throwable exception) {
        super(message, exception);
    }
}