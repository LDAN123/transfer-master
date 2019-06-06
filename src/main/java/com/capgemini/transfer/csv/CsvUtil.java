package com.capgemini.transfer.csv;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CsvUtil {
    private static final String BIG_NUMBER_PATTERN = "[0-9.]{10,}|^0[0-9.]+";
	public static void writeCsv(String filePath,String[] header, List<String[]> rows) throws IOException{
	    Pattern bigNumberPattern = Pattern.compile(BIG_NUMBER_PATTERN);
		CSVFormat format = CSVFormat.DEFAULT.withHeader(header).withSkipHeaderRecord();
		CSVPrinter printer = null;
		Writer out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
			byte[] bom = {(byte) 0xEF, (byte) 0xBB,(byte) 0xBF};//{(byte)0xFF, (byte)0xFE};
			out.write(new String(bom,"UTF-8"));
			printer = new CSVPrinter(out, format);
			if(header != null && header.length > 0){
				printer.printRecord(header);
			}
			for(String[] row : rows){
			    if(row == null || row.length==0){
			        continue;
			    }
			    for(int i=0;i<row.length;i++){
			        if(row[i] != null && bigNumberPattern.matcher(row[i]).matches()){
			            row[i] += "\t";
			        }
			    }
			}
	        printer.printRecords(rows);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(printer != null){
				try{
					printer.close();
				}catch(Exception e){}
			}
			if(out != null){
				try{
					out.close();
				}catch(Exception e){}
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
	    String filePath="d:/testcsv.csv";
	    String[] header={"a","b","c","d"};
	    List<String[]> rows = new ArrayList<String[]>();
	    rows.add(new String[]{"1111111111111111111","12345678901234567890","123456789","1234567890"});
	    rows.add(new String[]{"2222222222222222222","0000","000.10000100","000.0212121212100000000"});
	    rows.add(new String[]{"3333","00.232000000","12121212121212121212121","1234567890"});
	    rows.add(new String[]{"00212","0011111","00000000000000011111111000000","0.12345678901234567890123456789"});
	    writeCsv(filePath,header,rows);
	}
}
