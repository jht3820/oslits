package kr.opensoftlab.sdf.excel;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import kr.opensoftlab.sdf.excel.vo.ExcelMergeVO;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.DateUtil;


/**
 * POI OOXML(spreadsheetml)기반의 XML 데이터를 만든다..
 *
 * @author Jason Park
 * @version 1.0
 * 
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
public class XMLSheetWriter {

    private final Writer _out;
    private int _rownum;
    
    private String _merge = null;

    public XMLSheetWriter(Writer out) {

        _out = out;
    }

    public void beginSheet() throws IOException {
        _out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
        _out.write("<sheetData>\n");
    }

    public void endSheet() throws IOException {
        _out.write("</sheetData>");
        if(_merge != null)
            _out.write(_merge);
        _out.write("</worksheet>");
    }

    /**
     * Insert a new row
     * 
     * @param rownum
     *            0-based row number
     */
    public void insertRow(int rownum) throws IOException {
        _out.write("<row r=\"" + (rownum + 1) + "\">\n");
        this._rownum = rownum;
    }

    /**
     * Insert row end marker
     */
    public void endRow() throws IOException {
        _out.write("</row>\n");
    }

    public void createCell(int columnIndex, String value, int styleIndex)
            throws IOException {
        String ref = new CellReference(_rownum, columnIndex).formatAsString();
        ref = ref.replaceAll("\\$", "");
        if(value == null)
            value = "";
        _out.write("<c r=\"" + ref + "\" t=\"inlineStr\"");
        if (styleIndex != -1)
            _out.write(" s=\"" + styleIndex + "\"");
        _out.write(">");
        _out.write("<is><t><![CDATA[" + value + "]]></t></is>");
        _out.write("</c>");

    }

    public void createCell(int columnIndex, String value) throws IOException {
        createCell(columnIndex, value, -1);
    }

    public void createCell(int columnIndex, double value, int styleIndex)
            throws IOException {
        String ref = new CellReference(_rownum, columnIndex).formatAsString();
        ref = ref.replaceAll("\\$", "");
        _out.write("<c r=\"" + ref + "\" t=\"n\"");
        if (styleIndex != -1)
            _out.write(" s=\"" + styleIndex + "\"");
        _out.write(">");
        _out.write("<v><![CDATA[" + value + "]]></v>");
        _out.write("</c>");

    }

    public void createCell(int columnIndex, double value) throws IOException {
        createCell(columnIndex, value, -1);
    }

    public void createCell(int columnIndex, Date value, int styleIndex)
            throws IOException {
        createCell(columnIndex, DateUtil.getExcelDate(value, false), styleIndex);
    }
    
    /**
     * 엑셀 CellMerge
     * MS Excel 2007 기준.
     * <mergeCells><mergeCell/>..</mergeCells> 테그를 이용하여 엑셀시트를 Merge 함.
     * 엑셀 Column 좌표와 Rows 좌표를 이용하며 아래의 예제를 참고한다.
     * ex)
     * <mergeCells count="5">
     *  <mergeCell ref="A1:A2"/>
     *  <mergeCell ref="C1:D1"/>
     *  <mergeCell ref="B1:B2"/>
     *  <mergeCell ref="F1:H1"/>
     *  <mergeCell ref="E1:E2"/>
     * </mergeCells>
     * 1. A1, A2컬럼을 MERGE한다. -> ROWSPAN
     * 2. C1, D1컬럼을 MERGE한다. -> COLSPAN
     * 3. B1, B2컬럼을 MERGE한다. -> ROWSPAN
     * 등등..
     * 
     * @param arry
     * @throws IOException
     */
    public void mergeCell(List<ExcelMergeVO> arry) throws IOException{
        if(arry == null) return;
        
        int mergeCnt = arry.size();
        StringBuilder sb = new StringBuilder();
        sb.append("<mergeCells>");
        for(int i=0; i < mergeCnt; i++){
            sb.append("<mergeCell ref=\""+arry.get(i).getFROM()+ ":" + arry.get(i).getTO() + "\"/>");
        }
        sb.append("</mergeCells>");
        
        _merge = sb.toString();
    }
}