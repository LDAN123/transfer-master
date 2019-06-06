package com.capgemini.transfer.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.capgemini.transfer.config.Config.Ref;

public class ExcelConfigUtil {
	public static final String CONFIG_FILE="config_excel";
	
	public static Map<String,Config> parseConfig(String filePath) throws FileNotFoundException,IOException,Exception{
		File excel = new File(filePath);
		if(!excel.exists()){
			throw new FileNotFoundException(filePath);
		}
		Workbook workbook = null;
		FileInputStream in = new FileInputStream(excel);
		if(filePath.toLowerCase().endsWith(".xls") || filePath.toLowerCase().endsWith(".xlsx")){
			workbook = WorkbookFactory.create(in);
		}
		if(workbook == null){
			throw new Exception(filePath+" is not a excel");
		}
		
		Sheet sheet = null;
        Row row = null;
        
        int lastRowNum = 0;
        int numSheets = workbook.getNumberOfSheets();
        Map<String,Config> configMap = new HashMap<String,Config>();
        Config currentConfig = null;
        String[] rowValues = null;
        for(int i = 0; i < numSheets; i++) {
            sheet = workbook.getSheetAt(i);
            if(sheet.getPhysicalNumberOfRows() > 0) {
                lastRowNum = sheet.getLastRowNum();
                for(int j = 0; j <= lastRowNum; j++) {
                	if(j == 0){
                		continue;
                	}
                    row = sheet.getRow(j);
                    rowValues = getRowValues(row,6);
                    if(rowValues != null){
                    	currentConfig = configMap.get(rowValues[0]);
                    	if(currentConfig == null){
                    		currentConfig = new Config();
                    		currentConfig.setFileName(rowValues[0]);
                    		configMap.put(rowValues[0], currentConfig);
                    	}
                    	currentConfig.getRefs().add(new Ref(rowValues[1],rowValues[3],rowValues[2],Integer.parseInt(rowValues[4]),rowValues[5]));
                    }
                }
            }
        }
		workbook.close();
		try{in.close();}catch(Exception e){}
		return configMap;
	}
	private static String[] getRowValues(Row row,int len){
		Cell cell = null;
		if(row.getLastCellNum() < len-1){
			return null;
		}
		String[] result = new String[len];
		for(int i=0;i<len;i++){
			cell = row.getCell(i);
			if(cell == null || cell.getCellTypeEnum() == CellType.BLANK){
				return null;
	        }
			if(cell.getCellTypeEnum() == CellType.NUMERIC){
				result[i] = Double.valueOf(cell.getNumericCellValue()).intValue()+"";
			}else if(cell.getCellTypeEnum() == CellType.STRING){
				result[i] = cell.getStringCellValue().trim();
				if("".equals(result[i])){
					return null;
				}
			}
			//System.out.println(result[i]);
		}
		
		return result;
	}
}
