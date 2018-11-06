package kr.opensoftlab.sdf.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 엑셀을 어떻게 파싱해야할지의 정보를 담고있는 annotation 객체
 * <p>엑셀을 파싱하여 담고하는 Object 내의 멤버변수의 해당 annotation을 달아 엑셀 파싱정보를 전달해줄 수 있다.
 * (e.g. @ExcelColumn(position="3"))
 * @author 조용상
 * @version 1.0
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD })
public @interface ExcelColumn {
    /**
     * 엑셀의 여러 셀중에서 어떤 셀 값을 얻어올지 위치정보를 설정한다.
     * @return 엑셀 cell 위치 정보
     */
    int position();
    
    /**
     * 엑셀 cell 데이터 중 date 포멧에 대한 포멧형태를 설정한다.
     * @return date 포멧
     */
    String dateFormat() default "yyyyMMdd";

}