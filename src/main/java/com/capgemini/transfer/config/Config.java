package com.capgemini.transfer.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Config {
	private String fileName;
	private List<Ref> refs = new ArrayList<Ref>(); 
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<Ref> getRefs() {
		return refs;
	}
	public void setRefs(List<Ref> refs) {
		this.refs = refs;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{fileName: ").append(fileName)
			.append(" refs:[");
		for(Ref ref : refs){
			sb.append(ref.toString()).append(", ");
		}
		sb.append("]}");
		return sb.toString();
	}





	public static class Ref implements Comparable<Ref>{
		public static final String REPEAT_FLAG = "重复";
		public static final String NOT_REPEAT_FLAG = "非重复";
		public static final String STATIC_FLAG = "固定列值";
		public static final String DATE_TYPE_FLAG = "日期,";
		private String nameOfOBN;
		private String nameOfCSV;
		private String type;
		private int order;
		private String dataType;
		private DateFormat df = null;
		public Ref(String nameOfOBN,String nameOfCSV,String type,int order,String dataType){
			this.nameOfOBN = nameOfOBN;
			this.nameOfCSV = nameOfCSV;
			this.type = type;
			this.order = order;
			this.dataType = dataType;
			if(this.dataType != null && this.dataType.startsWith(DATE_TYPE_FLAG)){
				try{df = new SimpleDateFormat(this.dataType.substring(DATE_TYPE_FLAG.length()));}catch(Exception e){}
			}
		}
		public String getNameOfOBN() {
			return nameOfOBN;
		}
		public void setNameOfOBN(String nameOfOBN) {
			this.nameOfOBN = nameOfOBN;
		}
		public String getNameOfCSV() {
			return nameOfCSV;
		}
		public void setNameOfCSV(String nameOfCSV) {
			this.nameOfCSV = nameOfCSV;
		}
		public int getOrder() {
			return order;
		}
		public void setOrder(int order) {
			this.order = order;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDataType() {
			return dataType;
		}
		public DateFormat getDf() {
			return df;
		}
		public String toString(){
			return String.format("{nameOfOBN: %s, nameOfCSV: %s, type: %s, order: %s, dataType: %s}", this.nameOfOBN,this.nameOfCSV,this.type,this.order,this.dataType);
		}
		public int compareTo(Ref o) {
			return new Integer(this.order).compareTo(new Integer(o.order));
		}
		
		public boolean isRepeat() {
			return REPEAT_FLAG.equalsIgnoreCase(type);
		}
		public boolean isNotRepeat() {
			return NOT_REPEAT_FLAG.equalsIgnoreCase(type);
		}
		public boolean isStatic() {
			return STATIC_FLAG.equalsIgnoreCase(type);
		}
		public boolean isDateType() {
			return this.dataType != null && this.dataType.startsWith(DATE_TYPE_FLAG);
		}
	}
}
