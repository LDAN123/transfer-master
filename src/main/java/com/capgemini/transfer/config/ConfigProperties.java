package com.capgemini.transfer.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class ConfigProperties {
	/*private String userIds_username;
	private String userIds_password;
	private String userIds_url;*/
	
	private List<String> userIds_username_list = null;
	private List<String> userIds_password_list = null;
	private List<String> userIds_url_list = null;
	private List<String> userIds_property_name_list = null;
	private List<String> userIds_check_date_property_name_list = null;
	private List<String> userIds_check_same_property_name_list = null;
	private List<String> userIds_compare_date_property_name_list = null;
	
	private String userIds_check_transferred_username;
	private String userIds_check_transferred_password;
	private String userIds_check_transferred_url;
	private String userIds_check_transferred_key;
	private String userIds_check_transferred_target_property;
	private String userIds_check_transferred_target_userId;
	
	private int userIds_filter_day_num = 3;
	
	private String ws_url;
	private String ws_username;
	private String ws_password;
	
	private String csv_file_local_dir;
	
	private String ftp_host;
	private String ftp_username;
	private String ftp_password;
	private String ftp_upload_dir;
	private boolean ftp_sftp = false;
	private int ftp_port = 21;
	
	private String user_info_english_chinese_ONB_field;
	private String user_info_english_pattern;
	private String user_info_english_date_format;
	private String user_info_chinese_date_format;
	private String user_info_date_format;
	
	private DateFormat user_info_english_date_format_df = null;
	private DateFormat user_info_chinese_date_format_df = null;
	private DateFormat[] user_info_date_format_df = null;
	
	private String app_schedule_start_time;
	private long app_schedule_time = 86400000;
	
	private int app_error_try_times = 10;
	
	private boolean app_run_task_on_start = false;
	
	private String userIds_whether_delete;
	
	private String userIds_ONB_check_delete_target_property;
	
	private String userIds_delete_entity;
	
	private String entity_property;
	
	private String userIds_EC_check_delete_target_property;
	
	private String ec_url;
	
	private ConfigProperties(){}
	
	private static ConfigProperties configProperties = new ConfigProperties();
	
//	static{
//		try{
//			load(ConfigProperties.class.getResource("accout_url.properties").getFile());
//		}catch(Exception e){}
//	}
	
	public static void load(String propertiesFile){
		File file = new File(propertiesFile);
		//System.out.println(file.getAbsolutePath());
		if(file.exists()){
			Properties p = new Properties();
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				p.load(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(fis != null){
					try{
						fis.close();
					}catch(Exception e){}
				}
			}
			System.out.println("properties file config: "+p);
			String value = p.getProperty("userIds_username");
//			if(value != null && !value.trim().isEmpty()){
//				configProperties.userIds_username = value.trim();
//			}
//			value = p.getProperty("userIds_password");
//			if(value != null && !value.trim().isEmpty()){
//				configProperties.userIds_password = value.trim();
//			}
//			value = p.getProperty("userIds_url");
//			if(value != null && !value.trim().isEmpty()){
//				configProperties.userIds_url = value.trim();
//			}
			
			int len = p.size();
			String url = null,username=null,pwd = null,get_property_name=null,
							check_date_property_name=null,check_same_property_name=null,userIds_compare_date_property_name=null;
			List<String> userIds_username_list = new ArrayList<String>();
			List<String> userIds_password_list = new ArrayList<String>();
			List<String> userIds_url_list = new ArrayList<String>();
			List<String> userIds_property_name_list = new ArrayList<String>();
			List<String> userIds_check_date_property_name_list = new ArrayList<String>();
			List<String> userIds_check_same_property_name_list = new ArrayList<String>();
			List<String> userIds_compare_date_property_name_list = new ArrayList<String>();
			for(int i=0;i<len;i++){
				url = p.getProperty("userIds_url_"+i);
				if(url == null || url.trim().isEmpty()){
					continue;
				}
				get_property_name = p.getProperty("userIds_property_name_"+i);
				if(get_property_name == null || get_property_name.trim().isEmpty()){
					continue;
				}
				username = p.getProperty("userIds_username_"+i);
				pwd = p.getProperty("userIds_password_"+i);
				
				userIds_url_list.add(url);
				userIds_username_list.add(username);
				userIds_password_list.add(pwd);
				
				check_date_property_name = p.getProperty("userIds_check_date_property_name_"+i);
				check_same_property_name = p.getProperty("userIds_check_same_property_name_"+i);
				userIds_compare_date_property_name = p.getProperty("userIds_compare_date_property_name_"+i);
				
				userIds_property_name_list.add(get_property_name);
				userIds_check_date_property_name_list.add(check_date_property_name);
				userIds_check_same_property_name_list.add(check_same_property_name);
				userIds_compare_date_property_name_list.add(userIds_compare_date_property_name);
			}
			configProperties.userIds_url_list = userIds_url_list;
			configProperties.userIds_username_list = userIds_username_list;
			configProperties.userIds_password_list = userIds_password_list;
			
			configProperties.userIds_property_name_list = userIds_property_name_list;
			configProperties.userIds_check_date_property_name_list = userIds_check_date_property_name_list;
			configProperties.userIds_check_same_property_name_list = userIds_check_same_property_name_list;
			configProperties.userIds_compare_date_property_name_list = userIds_compare_date_property_name_list;
			
			value = p.getProperty("userIds_filter_day_num");
			if(value != null && !value.trim().isEmpty()){
				try{
					configProperties.userIds_filter_day_num = Integer.parseInt(value.trim());
				}catch(Exception e){}
			}
			
			value = p.getProperty("userIds_check_transferred_username");
			if(value != null && !value.trim().isEmpty()){
				configProperties.userIds_check_transferred_username = value.trim();
			}
			value = p.getProperty("userIds_check_transferred_password");
			if(value != null && !value.trim().isEmpty()){
				configProperties.userIds_check_transferred_password = value.trim();
			}
			value = p.getProperty("userIds_check_transferred_url");
			if(value != null && !value.trim().isEmpty()){
				configProperties.userIds_check_transferred_url = value.trim();
			}
			value = p.getProperty("userIds_check_transferred_key");
			if(value != null && !value.trim().isEmpty()){
				configProperties.userIds_check_transferred_key = value.trim();
			}
			value = p.getProperty("userIds_check_transferred_target_property");
			if(value != null && !value.trim().isEmpty()){
				configProperties.userIds_check_transferred_target_property = value.trim();
			}
			value = p.getProperty("userIds_check_transferred_target_userId");
			if(value != null && !value.trim().isEmpty()){
				configProperties.userIds_check_transferred_target_userId = value.trim();
			}
			
			
			value = p.getProperty("ws_url");
			if(value != null && !value.trim().isEmpty()){
				configProperties.ws_url = value.trim();
			}
			value = p.getProperty("ws_username");
			if(value != null && !value.trim().isEmpty()){
				configProperties.ws_username = value.trim();
			}
			value = p.getProperty("ws_password");
			if(value != null && !value.trim().isEmpty()){
				configProperties.ws_password = value.trim();
			}
			
			value = p.getProperty("csv_file_local_dir");
			if(value != null && !value.trim().isEmpty()){
				configProperties.csv_file_local_dir = value.trim();
			}
			
			value = p.getProperty("ftp_host");
			if(value != null && !value.trim().isEmpty()){
				configProperties.ftp_host = value.trim();
			}
			value = p.getProperty("ftp_username");
			if(value != null && !value.trim().isEmpty()){
				configProperties.ftp_username = value.trim();
			}
			value = p.getProperty("ftp_password");
			if(value != null && !value.trim().isEmpty()){
				configProperties.ftp_password = value.trim();
			}
			value = p.getProperty("ftp_upload_dir");
			if(value != null && !value.trim().isEmpty()){
				configProperties.ftp_upload_dir = value.trim();
			}
			value = p.getProperty("ftp_port");
			if(value != null && !value.trim().isEmpty()){
				try{
					configProperties.ftp_port = Integer.parseInt(value.trim());
				}catch(Exception e){}
			}
			value = p.getProperty("ftp_sftp");
			if(value != null && !value.trim().isEmpty()){
				configProperties.ftp_sftp = "sftp".equalsIgnoreCase(value.trim());
			}
			
			value = p.getProperty("user_info_english_chinese_ONB_field");
			if(value != null && !value.trim().isEmpty()){
				configProperties.user_info_english_chinese_ONB_field = value.trim();
			}
			value = p.getProperty("user_info_english_pattern");
			if(value != null && !value.trim().isEmpty()){
				configProperties.user_info_english_pattern = value.trim();
			}
			value = p.getProperty("user_info_english_date_format");
			if(value != null && !value.trim().isEmpty()){
				configProperties.user_info_english_date_format = value.trim();
				try{
					configProperties.user_info_english_date_format_df = new SimpleDateFormat(configProperties.user_info_english_date_format);
				}catch(Exception e){}
			}
			value = p.getProperty("user_info_chinese_date_format");
			if(value != null && !value.trim().isEmpty()){
				configProperties.user_info_chinese_date_format = value.trim();
				try{
					configProperties.user_info_chinese_date_format_df = new SimpleDateFormat(configProperties.user_info_chinese_date_format);
				}catch(Exception e){}
			}
			value = p.getProperty("user_info_date_format");
			if(value != null && !value.trim().isEmpty()){
				configProperties.user_info_date_format = value.trim();
				try{
					String[] dfs = configProperties.user_info_date_format.split(",");
					configProperties.user_info_date_format_df = new DateFormat[dfs.length];
					for(int i=0;i<dfs.length;i++){
						try{
							configProperties.user_info_date_format_df[i] = new SimpleDateFormat(dfs[i]);
						}catch(Exception e){}
					}
				}catch(Exception e){}
			}
			value = p.getProperty("app_schedule_start_time");
			if(value != null && !value.trim().isEmpty()){
				configProperties.app_schedule_start_time = value.trim();
			}
			value = p.getProperty("app_schedule_time");
			if(value != null && !value.trim().isEmpty()){
				try{
					configProperties.app_schedule_time = Long.parseLong(value.trim());
				}catch(Exception e){}
			}
			
			value = p.getProperty("app_error_try_times");
			if(value != null && !value.trim().isEmpty()){
				try{
					configProperties.app_error_try_times = Integer.parseInt(value.trim());
				}catch(Exception e){}
			}
			
			value = p.getProperty("app_run_task_on_start");
			if(value != null && !value.trim().isEmpty()){
				try{
					configProperties.app_run_task_on_start = Boolean.parseBoolean(value.trim());
				}catch(Exception e){}
			}
			
			value = p.getProperty("userIds_whether_delete");
			if(value != null && !value.trim().isEmpty()){
				configProperties.userIds_whether_delete = value.trim();
			}
			
			value = p.getProperty("userIds_ONB_check_delete_target_property");
			if(value != null && !value.trim().isEmpty()){
				configProperties.userIds_ONB_check_delete_target_property = value.trim();
			}
			
			value = p.getProperty("userIds_delete_entity");
			if(value != null && !value.trim().isEmpty()){
				configProperties.userIds_delete_entity = value.trim();
			}
			
			value = p.getProperty("Entity_property");
			if(value != null && !value.trim().isEmpty()){
				configProperties.entity_property = value.trim();
			}
			
			value = p.getProperty("userIds_EC_check_delete_target_property");
			if(value != null && !value.trim().isEmpty()){
				configProperties.userIds_EC_check_delete_target_property = value.trim();
			}
			value = p.getProperty("ec_url");
			if(value != null && !value.trim().isEmpty()){
				configProperties.ec_url = value.trim();
			}
		}
		
	}
	
	public static List<String> getUserIds_property_name_list() {
		return configProperties.userIds_property_name_list;
	}
	public static List<String> getUserIds_check_date_property_name_list() {
		return configProperties.userIds_check_date_property_name_list;
	}
	public static List<String> getUserIds_check_same_property_name_list() {
		return configProperties.userIds_check_same_property_name_list;
	}
	public static List<String> getUserIds_compare_date_property_name_list() {
		return configProperties.userIds_compare_date_property_name_list;
	}
	public static List<String> getUserIds_username_list() {
		return configProperties.userIds_username_list;
	}
	public static List<String> getUserIds_password_list() {
		return configProperties.userIds_password_list;
	}
	public static List<String> getUserIds_url_list() {
		return configProperties.userIds_url_list;
	}
	public static int getUserIds_filter_day_num() {
		return configProperties.userIds_filter_day_num;
	}
	public static String getUserIds_check_transferred_username() {
		return configProperties.userIds_check_transferred_username;
	}
	public static String getUserIds_check_transferred_password() {
		return configProperties.userIds_check_transferred_password;
	}
	public static String getUserIds_check_transferred_url() {
		return configProperties.userIds_check_transferred_url;
	}
	public static String getUserIds_check_transferred_key() {
		return configProperties.userIds_check_transferred_key;
	}
	public static String getUserIds_check_transferred_target_property() {
		return configProperties.userIds_check_transferred_target_property;
	}
	public static String getUserIds_check_transferred_target_userId() {
		return configProperties.userIds_check_transferred_target_userId;
	}

	public static String getWs_url() {
		return configProperties.ws_url;
	}
	public static String getWs_username() {
		return configProperties.ws_username;
	}
	public static String getWs_password() {
		return configProperties.ws_password;
	}
	public static String getCsv_file_local_dir() {
		return configProperties.csv_file_local_dir;
	}
	public static String getFtp_host() {
		return configProperties.ftp_host;
	}
	public static String getFtp_username() {
		return configProperties.ftp_username;
	}
	public static String getFtp_password() {
		return configProperties.ftp_password;
	}
	public static String getFtp_upload_dir() {
		return configProperties.ftp_upload_dir;
	}
	public static int getFtp_port() {
		return configProperties.ftp_port;
	}
	public static boolean isSftp() {
		return configProperties.ftp_sftp;
	}

	public static String getUser_info_english_chinese_ONB_field() {
		return configProperties.user_info_english_chinese_ONB_field;
	}

	public static String getUser_info_english_pattern() {
		return configProperties.user_info_english_pattern;
	}

	public static String getUser_info_english_date_format() {
		return configProperties.user_info_english_date_format;
	}

	public static String getUser_info_chinese_date_format() {
		return configProperties.user_info_chinese_date_format;
	}

	public static String getUser_info_date_format() {
		return configProperties.user_info_date_format;
	}

	public static String getApp_schedule_start_time() {
		return configProperties.app_schedule_start_time;
	}

	public static long getApp_schedule_time() {
		return configProperties.app_schedule_time;
	}
	
	public static int getApp_error_try_times() {
		return configProperties.app_error_try_times;
	}
	
	public static boolean getApp_run_task_on_start() {
		return configProperties.app_run_task_on_start;
	}
	
	public static String getUserIds_whether_delete() {
		return configProperties.userIds_whether_delete;
	}
	
	public static String getUserIds_ONB_check_delete_target_property() {
		return configProperties.userIds_ONB_check_delete_target_property;
	}

	public static String getUserIds_delete_entity() {
		return configProperties.userIds_delete_entity;
	}

	public static String getUserIds_EC_check_delete_target_property() {
		return configProperties.userIds_EC_check_delete_target_property;
	}

	public static String getEntity_property() {
		return configProperties.entity_property;
	}
	
	public static String getEc_url() {
		return configProperties.ec_url;
	}
	
	public static String formatEnglishDate(Date date){
		if(configProperties.user_info_english_date_format_df == null){
			return null;
		}
		return configProperties.user_info_english_date_format_df.format(date);
	}
	public static String formatChineseDate(Date date){
		if(configProperties.user_info_chinese_date_format_df == null){
			return null;
		}
		return configProperties.user_info_chinese_date_format_df.format(date);
	}
	public static Date parseUserInfoDate(String date){
		Date d = null;
		for(DateFormat df : configProperties.user_info_date_format_df){
			try{
				d = df.parse(date);
			}catch(Exception e){}
			if(d != null){
				return d;
			}
		}
		return null;
	}
}
