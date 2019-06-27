package com.example.demo.util;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author chengdu
 * @date 2019/6/28.
 */
public class ExcelTask implements Callable<ExcelResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelTask.class);

    private Sheet sheet;
    private CellStyle cellStyle;
    private String[] titles;
    private List<String[]> values;
    private int begin;

    public ExcelTask(Sheet sheet, CellStyle cellStyle, String[] titles, List<String[]> values, int begin) {
        this.sheet = sheet;
        this.cellStyle = cellStyle;
        this.titles = titles;
        this.values = values;
        this.begin = begin;
    }

    @Override
    public ExcelResult call() throws Exception {
        ExcelResult excelResult = new ExcelResult();
        String sheetName = sheet.getSheetName();
        excelResult.setSheetName(sheetName);
        try {
            long startTime = System.currentTimeMillis();
            LOGGER.info(Thread.currentThread().getName() + " start export sheet {}, time {}", sheetName, startTime);
            ExcelUtil.mergeCellTest(sheet, cellStyle);
            ExcelUtil.fillSheetData(sheet, cellStyle, titles, values, 1);
            LOGGER.info(Thread.currentThread().getName() + " end export sheet {}, time interval {}", sheetName,
                    System.currentTimeMillis() - startTime);
            excelResult.setStatus(true);
        }catch (Exception e){
            LOGGER.info("export sheet {} error", sheetName);
            excelResult.setStatus(false);
        }
        return excelResult;
    }
}
