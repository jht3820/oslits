package kr.opensoftlab.sdf.excel;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import kr.opensoftlab.sdf.excel.file.FileDownloadView;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.ModelAndView;



/**
 * POI OOXML(spreadsheetml)기반의 XMLSheetWriter를 이용하여 Excel 파일을 만든다.
 * XML파일을 생성 후 템플릿 파일을 이용하여 엑셀파일로 변환한다.
 *
 * @author Jason Park
 * @version 1.0
 * 
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
public class BigDataSheetWriter {
    
    private String sheetName;
    private String path;
    private String excelName;
    private List<Metadata> metadatas;
    
    private Map<String, XSSFCellStyle> styles;
    private String sheetRef;
    
    private XSSFWorkbook wb;
    private XMLSheetWriter sw;
    private Writer fw;
    
    private File _targetFile;
    private File _tempFile;
    private File _xml;
    private FileOutputStream _tempFileOutputStream;
    
    private ModelAndView modelAndView;
    
    /**
     * 초기 생성자.
     * @param sheetName 엑셀에 표시될 Sheet명
     * @param path 엑셀파일을 생성할 서버 경로
     * @param excelName 사용자에게 보여지는 엑셀 파일 이름
     * @param metadatas Controller에서 생성한 Metadata List
     */
    public BigDataSheetWriter(String sheetName, String path, String excelName, List<Metadata> metadatas) {
        this.sheetName = sheetName;
        this.path = path;
        this.metadatas = metadatas;
        this.excelName = excelName;
    }
    
    public void beginSheet() {
        this.wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(this.sheetName);
        
        createStyles(this.wb, this.metadatas);
        //name of the zip entry holding sheet data, e.g. /xl/worksheets/sheet1.xml
        sheetRef = sheet.getPackagePart().getPartName().getName();

        //file path
        String targetSheetFilePath = path;
        String templeteSheetFilePath = path;
        
        /**
         * 1. 엑셀 대상 디렉토리 및 템프 디렉토리 검사
         *    디렉토리 미 생성시 신규 생성.
         */
        
        File _targetdir = new File(targetSheetFilePath);
        File _tempdir = new File(templeteSheetFilePath);
        
        if(!_targetdir.isDirectory()) _targetdir.mkdir();
        if(!_tempdir.isDirectory()) _tempdir.mkdir();
        
        /**
         * 2. target 엑셀파일 생성.
         *    temp   XML파일 생성.
         */
        try {
            _targetFile = File.createTempFile("excelDownTemp", ".xlsx", _targetdir);
            _tempFile = File.createTempFile("TMP", ".xlsx", _tempdir);
            _tempFileOutputStream = new FileOutputStream(_tempFile);
            wb.write(_tempFileOutputStream);
            _xml = File.createTempFile("excelDownTemp", ".xml", _tempdir);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        } finally {
            if(_tempFileOutputStream != null)
                try {
                    _tempFileOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }

        /**
         * 3. 데이터를 fatch하여 temp XML(POI OOXML) 생성. 
         *    fatch 구현은 Controller단에서 Mybatis ResultHandler를 이용
         */
        try {
            fw = new OutputStreamWriter(new FileOutputStream(_xml), "UTF-8");
            sw = new XMLSheetWriter(fw);
            sw.beginSheet();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void endSheet() {
        try {
            sw.endSheet();
            fw.close();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally{
            try {
                if(fw != null)
                    fw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        FileOutputStream out = null;
        
        try{
            out = new FileOutputStream(_targetFile);
            Map<String, File> sheetMap = new HashMap<String, File>();
            sheetMap.put(sheetRef.substring(1), _xml);
            substitute(_tempFile, sheetMap, out);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }finally{
            try {
                if (out != null)
                    out.close();
                if(_tempFile != null)
                    _tempFile.delete();
                if(_xml != null)
                    _xml.delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.hhmm");
        String targetName = this.excelName + "_" + sdf.format(date) + ".xlsx";
        
        modelAndView = new ModelAndView(new FileDownloadView());
        modelAndView.addObject(FileDownloadView.FILE_ATTRIBUTE_NAME, _targetFile);
        modelAndView.addObject(FileDownloadView.FILENAME_ATTRIBUTE_NAME, targetName);
    }
    
    public ModelAndView getModelAndView() {
        return this.modelAndView;
    }
    public XMLSheetWriter getXMLSheetWriter() {
        return this.sw;
    }
    public Map<String, XSSFCellStyle> getStyleMap() {
        return this.styles;
    }
        
    /**
     * 대용량 엑셀 
     * 스타일MAP생성
     * @param wb
     * @param metadatas
     * @return
     */
     private void createStyles(XSSFWorkbook wb, List<Metadata> metadatas){
         styles = new HashMap<String, XSSFCellStyle>();
        
         XSSFCellStyle header = wb.createCellStyle();
         XSSFFont font = wb.createFont();
         font.setBold(true);
         font.setFontHeight(10);
         font.setFontName("맑은 고딕");
         header.setFillForegroundColor(new XSSFColor(new Color(255, 224, 140)));
         header.setFillPattern(CellStyle.SOLID_FOREGROUND);
         header.setBorderRight(CellStyle.BORDER_THIN);              //테두리 설정   
         header.setBorderLeft(CellStyle.BORDER_THIN);   
         header.setBorderTop(CellStyle.BORDER_THIN);
         header.setBorderBottom(CellStyle.BORDER_THIN);   
         header.setFont(font);
         styles.put("header", header);
        
         DataFormat dataFormat= wb.createDataFormat();
         for (Metadata metadata : metadatas) {
             XSSFCellStyle style = wb.createCellStyle();
             if (metadata.getFormat() != null) {
                 style.setDataFormat(dataFormat.getFormat(metadata.getFormat()));
             }
             style.setAlignment(metadata.getAlignment());
             styles.put(metadata.getName(), style);
         }
    }
     
     /**
      * 대용량 엑셀 
      * @param zipfile the template file
      * @param tmpfile the XML file with the sheet data
      * @param entry the name of the sheet entry to substitute, e.g. xl/worksheets/sheet1.xml
      * @param out the stream to write the result to
      */
      @SuppressWarnings({ "rawtypes", "resource" })
      private void substitute(File zipfile, Map sheetMap, OutputStream out)
              throws IOException {
      
          ZipFile zip = new ZipFile(zipfile);
    
          ZipOutputStream zos = new ZipOutputStream(out);
          @SuppressWarnings("unchecked")
          Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
          while (en.hasMoreElements()) {
              ZipEntry ze = en.nextElement();
              if (!sheetMap.containsKey(ze.getName())) {
                  zos.putNextEntry(new ZipEntry(ze.getName()));
                  InputStream is = zip.getInputStream(ze);
                  copyStream(is, zos);
                  is.close();
              }
          }
    
          Iterator it = sheetMap.keySet().iterator();
          while (it.hasNext()) {
              String entry = (String) it.next();
              System.out.println(entry);
              zos.putNextEntry(new ZipEntry(entry));
              InputStream is = new FileInputStream((File) sheetMap.get(entry));
              copyStream(is, zos);
              is.close();
          }
          zos.close();
      }
      
     /**
      * 대용량 엑셀 
      * @param in
      * @param out
      * @throws IOException
      */
     private void copyStream(InputStream in, OutputStream out) throws IOException {
         byte[] chunk = new byte[1024];
         int count;
         while ((count = in.read(chunk)) >=0 ) {
           out.write(chunk,0,count);
         }
     }
}