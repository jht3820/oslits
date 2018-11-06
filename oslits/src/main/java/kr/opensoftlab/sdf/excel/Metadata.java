package kr.opensoftlab.sdf.excel;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class Metadata {
    private String name;
    private short alignment;
    private String datePattern;
    private String format;

    public Metadata(String name) {
        this(name, XSSFCellStyle.ALIGN_LEFT);
    }
    
    public Metadata(String name, short alignment) {
        this(name, alignment, null, null);
    }
    
    public Metadata(String name, String format) {
        this(name, XSSFCellStyle.ALIGN_LEFT, null, format);
    }

    public Metadata(String name, String datePattern, String format) {
        this(name, XSSFCellStyle.ALIGN_LEFT, datePattern, format);
    }

    public Metadata(String name, short alignment, String format) {
        this(name, alignment, null, format);
    }

    public Metadata(String name, short alignment, String datePattern, String format) {
        this.name = name;
        this.alignment = alignment;
        this.datePattern = datePattern;
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getAlignment() {
        return alignment;
    }
    
    public void setAlignment(short alignment) {
        this.alignment = alignment;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String pattern) {
        this.datePattern = pattern;
    }

    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
}