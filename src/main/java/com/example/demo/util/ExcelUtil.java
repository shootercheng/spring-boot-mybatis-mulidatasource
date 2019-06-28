package com.example.demo.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.extractor.ExcelExtractor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author chengdu
 * @date 2019/6/27.
 */
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    public static final String EXCEL_XLS = ".xls";
    public static final String EXCEL_XLSX = ".xlsx";

    /**
     * 读取模板导出Excel
     * @param templatePath
     * @param rowoffset
     * @param cellvalues
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbookByTemplatePath(String templatePath, int rowoffset, List<String[]> cellvalues) throws Exception{
        InputStream inputStream = null;
        Workbook workbook = null;
        try{
            inputStream = new FileInputStream(templatePath);
            if(templatePath.endsWith(EXCEL_XLS)){
                workbook = new HSSFWorkbook(inputStream);
            }else if(templatePath.endsWith(EXCEL_XLSX)){
                workbook = new XSSFWorkbook(inputStream);
            }else{
                inputStream.close();
                throw new RuntimeException("Excel 文件格式不正确-----"+templatePath);
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
        }

        //获取 sheet页
        Sheet sheet = workbook.getSheetAt(0);
        //创建内容行
        for(int i=0; i < cellvalues.size(); i++){
            Row valuerow = sheet.createRow(i + rowoffset);
            String[] values = cellvalues.get(i);
            for(int j = 0; j < values.length; j++){
                Cell cell = valuerow.createCell(j);
                cell.setCellValue(values[j]);
            }
        }
        return workbook;
    }

    /**
     * 创建 workbook
     * @param filePath
     * @return
     */
    public static Workbook createWorkBook(String filePath){
        if(filePath == null){
            throw new IllegalArgumentException("filePath is null");
        }
        Workbook workbook;
        if(filePath.endsWith(EXCEL_XLS)){
            workbook = new HSSFWorkbook();
        }else if(filePath.endsWith(EXCEL_XLSX)){
//            workbook = new XSSFWorkbook();
            workbook = new SXSSFWorkbook();
        }else{
            throw new RuntimeException("excel type error");
        }
        return workbook;
    }

    /**
     * 创建 sheet 页
     * @param workbook
     * @param sheetNames
     */
    public static void createSheets(Workbook workbook, String[] sheetNames){
        for(String sheetname : sheetNames){
            workbook.createSheet(sheetname);
        }
    }

    public static void mergeCellTest(Sheet sheet, CellStyle cellStyle){
        Row row = sheet.createRow(0);
        for(int i = 0; i < 10; i++){
            row.createCell(i);
        }
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));
        sheet.addMergedRegion(new CellRangeAddress(0,0,5,9));
        Cell cell1 = row.getCell(0);
        Cell cell2 = row.getCell(5);
        cell1.setCellValue("MergeCell1");
        cell2.setCellValue("MergeCell2");
        cell1.setCellStyle(cellStyle);
        cell2.setCellStyle(cellStyle);
    }

    /**
     * 创建 sheet 数据
     * @param sheet
     * @param cellStyle
     * @param titles
     * @param values
     * @param begin
     */
    public static void fillSheetData(Sheet sheet, CellStyle cellStyle, String[] titles, List<String[]> values, int begin){

        // 创建标题行, 添加样式
        Row row = sheet.createRow(begin);
        for(int i = 0; i < titles.length; i++){
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(titles[i]);
        }

        // 创建内容行
        for(int i = 0; i < values.size(); i++){
            Row valueRow = sheet.createRow(begin + 1 + i);
            String[] rowDatas = values.get(i);
            for(int j = 0; j < rowDatas.length; j++) {
                Cell cell = valueRow.createCell(j);
                cell.setCellValue(rowDatas[j]);
            }
        }

    }

    public static void setCellStyle(CellStyle style){
        // 设置这些样式
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        style.setAlignment(HorizontalAlignment.CENTER);//水平布局：居中
    }

    public static List<String[]> createData(int rowSize, int colSize){
        List<String[]> excelValueList = new ArrayList<>(rowSize);
        for(int i = 0; i < rowSize; i++){
            String[] rowData = new String[colSize];
            for(int j = 0; j < colSize; j++){
                rowData[j] = "data_" + i + "_" + j;
            }
            excelValueList.add(rowData);
        }
        return excelValueList;
    }

    public static void testExcel() throws Exception {
        long startTime = System.currentTimeMillis();
        String filename = "C:/Users/chengdu/Desktop/excel/" + System.currentTimeMillis() + ".xlsx";
        Workbook workbook = createWorkBook(filename);
        String[] sheets = {"Page1","Page2"};
        createSheets(workbook, sheets);
        CellStyle cellStyle = workbook.createCellStyle();
        setCellStyle(cellStyle);
        long createSheesTime = System.currentTimeMillis();
        LOGGER.info("create sheet time {}", createSheesTime - startTime);
        String[] titles = {"col1","col2","col3","col4","col5",
                "col6","col7","col8","col9","col10"};
        List<String[]> valueList = createData(500000, titles.length);
        // Page 1
        Sheet sheet0 = workbook.getSheetAt(0);
        mergeCellTest(sheet0, cellStyle);
        fillSheetData(sheet0, cellStyle, titles, valueList, 1);
        long sheet0EndTime = System.currentTimeMillis();
        LOGGER.info("export sheet {} time {}", sheets[0], sheet0EndTime - createSheesTime);
        // Page 2
        Sheet sheet1 = workbook.getSheetAt(1);
        mergeCellTest(sheet1, cellStyle);
        fillSheetData(sheet1, cellStyle, titles, valueList, 1);
        long sheet1EndTime = System.currentTimeMillis();
        LOGGER.info("export sheet {} time {}", sheets[1], sheet1EndTime - sheet0EndTime);
        OutputStream fileOutputStream = new FileOutputStream(filename);
        workbook.write(fileOutputStream);
        LOGGER.info("write excel to stream time {}", System.currentTimeMillis() - sheet1EndTime);
    }

    public static void testExcelMutli() throws Exception {
        long startTime = System.currentTimeMillis();
        String filename = "C:/Users/chengdu/Desktop/excel/" + "multi_" + System.currentTimeMillis() + ".xlsx";
        Workbook workbook = createWorkBook(filename);
        int sumSheet = 50;
        String[] sheets = new String[sumSheet];
        for(int i = 0; i < sumSheet; i++){
            sheets[i] = "Page" + i;
        }
        createSheets(workbook, sheets);
        CellStyle cellStyle = workbook.createCellStyle();
        setCellStyle(cellStyle);
        String[] titles = {"col1","col2","col3","col4","col5",
                "col6","col7","col8","col9","col10"};
        List<String[]> valueList = createData(100000, titles.length);
        long createSheesTime = System.currentTimeMillis();
        LOGGER.info("create sheet time {}", createSheesTime - startTime);
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        List<Future<ExcelResult>> futureList = new ArrayList<>(sumSheet);
//        ExcelTask sheet0Task = new ExcelTask(workbook.getSheetAt(0),
//                cellStyle, titles, valueList, 1);
//        ExcelTask sheet1Task = new ExcelTask(workbook.getSheetAt(1),
//                cellStyle, titles, valueList, 1);
//        Future<ExcelResult> sheet0Result = threadPool.submit(sheet0Task);
//        Future<ExcelResult> sheet1Result = threadPool.submit(sheet1Task);
//        futureList.add(sheet0Result);
//        futureList.add(sheet1Result);
        for(int i = 0; i < sumSheet; i++){
            ExcelTask sheetTask = new ExcelTask(workbook.getSheetAt(i),
                cellStyle, titles, valueList, 1);
            Future<ExcelResult> future = threadPool.submit(sheetTask);
            futureList.add(future);
        }
        for(Future<ExcelResult> future : futureList){
            LOGGER.info("task result {}", future.get());
        }
        threadPool.shutdown();
        long writeStartTime = System.currentTimeMillis();
        OutputStream fileOutputStream = new FileOutputStream(filename);
        workbook.write(fileOutputStream);
        LOGGER.info("[testExcelMutli] write excel to stream time {}", System.currentTimeMillis() - writeStartTime);
    }

    public static void main(String[] args) throws Exception {
//        testExcel();
        testExcelMutli();
    }
}
