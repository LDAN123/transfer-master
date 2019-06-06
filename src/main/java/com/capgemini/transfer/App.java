package com.capgemini.transfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.capgemini.transfer.config.Config;
import com.capgemini.transfer.config.ConfigProperties;
import com.capgemini.transfer.config.ExcelConfigUtil;
import com.capgemini.transfer.csv.CsvUtil;
import com.capgemini.transfer.ftp.FtpUtil;
import com.capgemini.transfer.http.HttpUtil;
import com.capgemini.transfer.ws.client.UserInfoWsUtil;
import com.capgemini.transfer.xml.XmlUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
	public static AtomicInteger TASK_TIMES = new AtomicInteger(0);
	
	public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
		
		System.out.println("start app......");
//		args = new String[]{"D:/work/workspace/transfer/transfer/configExcel6.xlsx","D:/work/workspace/transfer/transfer/accout_url6.properties"};
		if(args == null || args.length < 2){
			System.out.println("need 2 arguments, first is field config excel, second is accounts and urls config file");
			System.exit(0);
		}
		ConfigProperties.load(args[1]);
//		System.out.println("afas dsf asd".matches(ConfigProperties.getUser_info_english_pattern()));
//		removeTransferredUserIds(new ArrayList<String>());
//		if("s".equals("s")){
//			return;
//		}
		
		try {
			if(ConfigProperties.getApp_run_task_on_start() || true){
				doIt(args[0]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Timer timer = new Timer();
		TransferTimeTask task = new TransferTimeTask(args[0], args[1]);
		SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf_date_time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date now = new Date();
		Date start_time = sdf_date_time.parse(sdf_date.format(now)+" "+ConfigProperties.getApp_schedule_start_time());
		while(start_time.getTime() < now.getTime()){
			start_time = DateUtils.addDays(start_time, 1);
		}
		System.out.println((start_time.getTime()-now.getTime())/1000+"秒后开始执行任务，之后任务间隔为"+(ConfigProperties.getApp_schedule_time()/1000)+"秒");
		timer.schedule(task, start_time.getTime()-now.getTime(), ConfigProperties.getApp_schedule_time());
//		
	}
	
	
	public static void doIt(String configExcelPath) throws FileNotFoundException, IOException, Exception{
		long startTime = System.currentTimeMillis();
		System.out.println("开始执行第"+TASK_TIMES.incrementAndGet()+"次任务......");
		//解析字段对应关系配置
		Map<String,Config> configs = ExcelConfigUtil.parseConfig(configExcelPath);
		Config config = null;
		boolean flag = true;
		int count = 0;
		final int tryTimes = ConfigProperties.getApp_error_try_times();
		/*//获取需要转换用户的id
		String url = ConfigProperties.getUserIds_url();//"https://api15.sapsf.cn/odata/v2/OnboardingCandidateInfo?$select=userId,readyToHire,managerId,kmsUserId,hrManagerId,hired,hireDate,fromExternalATS,candidateId,applicantId";
		String result = null;
		while(flag){
			try{
				result = HttpUtil.getWithBaseAuth(ConfigProperties.getUserIds_username(), ConfigProperties.getUserIds_password(), url);
				flag = false;
			}catch(Exception ee){
				System.out.println("get user list error: "+ee.getMessage()+", try "+(++count)+" times.");
				if(count > tryTimes){
					flag = false;
				}
			}
		}
		if(result == null || result.trim().length() < 100){
			System.out.println(result);
		}*/
		//获取需要转换用户的id
		List<String> userIds = getNeedDealUserIds();//XmlUtil.parseUserListXml(result);
		if(userIds == null || userIds.isEmpty()){
			System.out.println("未检索到符合要求的用户ID数据，退出本次任务。");
			System.out.println("结束第"+TASK_TIMES.get()+"次任务...... 用时："+((System.currentTimeMillis()-startTime)/1000)+"秒(S)");
//			userIds.add("79bed81e-93f9-4849-bd1e-9465feb87330");
			return;
		}
		//多次入职背景信息传输方案对应
		System.out.println("-------------------------------------------------------------------------------------------");

		if ("yes".equals(ConfigProperties.getUserIds_whether_delete())) {
			System.out.println("多次入职背景信息传输方案对应 开始");
			userBackgroundInfDel(userIds);
			System.out.println("多次入职背景信息传输方案对应 结束");
		} else {
			System.out.println("不执行 多次入职背景信息传输方案对应");
		}

		System.out.println("-------------------------------------------------------------------------------------------");
		System.out.println("get user id num: "+userIds.size());
		List<String> transferredUserIds = removeTransferredUserIds(userIds);
		/*String[] sssss = new String[]{"2017070420","2017080025","2017080031","2017080069","2017080085","2014070006","2017080010","2017080337",
				"2017080188","2017080189","2014120041","2017080204","2017080144","2017080141","2017080147","2017080143","2017070246",
				"2017070248","2017070323","2017070358","2017070050","2017080070"};
		transferredUserIds = Arrays.asList(sssss);*/
		System.out.println("transferred user id num: "+transferredUserIds.size());
		System.out.println("-------------------------------------------------------------------------------------------");
		
		List<String[]> results = null;
		for(Map.Entry<String, Config> e : configs.entrySet()){
			config = e.getValue();
			System.out.println("Excel config: "+config);
			Collections.sort(config.getRefs());
			flag = true;
			count = 0;
			while(flag){
				try{
					//调用WS获取用户的信息, 根据字段对应关系等配置信息解析ws返回的数据
					results = UserInfoWsUtil.getUserInfoXml(userIds, config.getRefs(), ConfigProperties.getWs_username(), ConfigProperties.getWs_password(),transferredUserIds);
					flag = false;
				}catch(Exception ee){
					System.out.println("call ws error: "+ee.getMessage()+", try "+(++count)+" times.");
					if(count > tryTimes){
						flag = false;
					}
				}
			}
			dealUserInfoRows(config, results);
		}
		
		System.out.println("结束第"+TASK_TIMES.get()+"次任务...... 用时："+((System.currentTimeMillis()-startTime)/1000)+"秒(S)");
	}

	private static void dealUserInfoRows(Config config, List<String[]> results) throws IOException{
		if(results == null || results.isEmpty()){
			System.out.println("未获取到信息，不进行文件生成和上传。");
			return;
		}
		List<String[]> results_chinese = new ArrayList<String[]>();
		List<String[]> results_english = new ArrayList<String[]>();
		
		boolean isEnglish = true;//是否为英文
//		String temp = ConfigProperties.getUser_info_english_chinese_ONB_field();//判断中英文的字段
//		String[] fields = new String[0];
//		if(temp != null && !temp.trim().isEmpty()){
//			fields = temp.trim().split(",");
//		}
		String[] temp = null;
		for(Iterator<String[]> iterator = results.iterator(); iterator.hasNext();){
			isEnglish = true;
			String[] ss = iterator.next();
			temp = new String[ss.length-1];
			System.arraycopy(ss, 0, temp, 0, temp.length);
			try{
				isEnglish = Boolean.parseBoolean(ss[ss.length-1]);
			}catch(Exception e){}
//			for (int i = 0; i < refs.size(); i++) {
//				for(String field : fields){
//					if(field.trim().equalsIgnoreCase(refs.get(i).getNameOfOBN())){
//						if(ss[i] != null && !ss[i].trim().matches(ConfigProperties.getUser_info_english_pattern())){
//							isEnglish = false;
//						}
//					}
//				}
//			}
			if(isEnglish){
				results_english.add(temp);
			}else{
				results_chinese.add(temp);
			}
		}
		
		String outputDir = ConfigProperties.getCsv_file_local_dir();
		String ftp_host = ConfigProperties.getFtp_host();
		int ftp_port = ConfigProperties.getFtp_port();
		String ftp_username = ConfigProperties.getFtp_username();
		String ftp_password = ConfigProperties.getFtp_password();
		String ftp_upload_dir = ConfigProperties.getFtp_upload_dir();
		
		String[] header = new String[config.getRefs().size()];
		for(int i=0;i< config.getRefs().size();i++){
			header[i] = config.getRefs().get(i).getNameOfCSV();
		}
		
		
		String csvName = config.getFileName();
		if(csvName == null || csvName.trim().isEmpty()){
			csvName = new Date().getTime()+"";
		}
		if(".csv".equalsIgnoreCase(csvName.substring(csvName.length()-4))){
			csvName = csvName.substring(0, csvName.length()-4);
		}
		String date_ymd = new SimpleDateFormat("yyyyMMdd").format(new Date());

		File file = null;
		List<String> csvNames = new ArrayList<String>(2);
		if(!results_english.isEmpty()){
			file = new File(outputDir, csvName+"_english"+date_ymd+".csv");
			//将解析的结果写入csv文件
			CsvUtil.writeCsv(file.getAbsolutePath(), header, results_english);
			csvNames.add(file.getAbsolutePath());
		}
		if(!results_chinese.isEmpty()){
			file = new File(outputDir, csvName+"_chinese"+date_ymd+".csv");
			//将解析的结果写入csv文件
			CsvUtil.writeCsv(file.getAbsolutePath(), header, results_chinese);
			csvNames.add(file.getAbsolutePath());
		}
		
		//通过ftp上传csv文件
		System.out.println("start ftp");
		int count = 0;
		final int tryTimes = ConfigProperties.getApp_error_try_times();
		if(ConfigProperties.isSftp()){
			boolean success = false;
			while(!success){
				success = FtpUtil.uploadFilesUseSftp(csvNames,ftp_upload_dir, ftp_host, ftp_port, ftp_username, ftp_password);
				if(count > tryTimes){
					success = true;
				}
				if(!success){
					System.out.println("ftp upload files error , try "+(++count)+" times.");
				}
			}
		}else{
			boolean success = false;
			while(!success){
				success = FtpUtil.uploadFilesUseFtp(csvNames, ftp_upload_dir, ftp_host, ftp_port, ftp_username, ftp_password);
				if(count > tryTimes){
					success = true;
				}
				System.out.println("ftp upload files error , try "+(++count)+" times.");
			}
			
		}
		System.out.println("end ftp");
	}
	
	public static void doIt(String configExcelPath, String accountUrlPropertiesFile) throws FileNotFoundException, IOException, Exception{
		ConfigProperties.load(accountUrlPropertiesFile);
		doIt(configExcelPath);
	}
	//获取已经传输过的用户id
	private static List<String> removeTransferredUserIds(List<String> userIds){
		boolean flag = true;
		int count = 0;
		final int tryTimes = ConfigProperties.getApp_error_try_times();
		String result = null;
		while(flag){
			try{
				result = HttpUtil.getWithBaseAuth(ConfigProperties.getUserIds_check_transferred_username(), ConfigProperties.getUserIds_check_transferred_password(), ConfigProperties.getUserIds_check_transferred_url());
				flag = false;
			}catch(Exception ee){
				System.out.println("get transferred user list error: "+ee.getMessage()+", try "+(++count)+" times.");
				if(count > tryTimes){
					flag = false;
				}
			}
		}
		System.out.println(result);
		try {
			List<String> transferredUserIds = XmlUtil.parseTransferredUserListXml(result,userIds);
			//userIds.removeAll(transferredUserIds);
			return transferredUserIds;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("transferred users interface return xml: "+result);
		} 
		
		return new ArrayList<String>();
	}
	//获取需要转换用户的id
	private static List<String> getNeedDealUserIds(){
		List<String> userIds_username_list = ConfigProperties.getUserIds_username_list();//http请求的用户名
		List<String> userIds_password_list = ConfigProperties.getUserIds_password_list();//http请求的秘密
		List<String> userIds_url_list = ConfigProperties.getUserIds_url_list();//http请求的url
		List<String> userIds_property_name_list = ConfigProperties.getUserIds_property_name_list();//http请求返回的xml中读取的属性名
		List<String> userIds_check_date_property_name_list = ConfigProperties.getUserIds_check_date_property_name_list();//http请求返回的xml中验证日期范围的属性名
		List<String> userIds_check_same_property_name_list = ConfigProperties.getUserIds_check_same_property_name_list();//http请求返回的xml中与前一个请求返回的数据进行内连接的属性名
		List<String> userIds_compare_date_property_name_list = ConfigProperties.getUserIds_compare_date_property_name_list();
		int len = userIds_url_list.size();
		Set<String> result_set = new HashSet<String>();
		List<String> result_temp = null;
		List<String> result_previous_step = new ArrayList<String>();
		String nextStartStr = "</entry><link rel=\"next\" href=\"";
		String nextEndStr = "\"></link></feed>";
		for(int i=0;i<len;i++){
			try{
				String url = userIds_url_list.get(i);
				List<String> result_current_step = new ArrayList<String>();
				while(true){
					String xml = getUserIdsXML(userIds_username_list.get(i),userIds_password_list.get(i),url);
					//System.out.println(xml);
					result_temp = XmlUtil.parseUserListXml(xml, result_previous_step, userIds_check_date_property_name_list.get(i), ConfigProperties.getUserIds_filter_day_num(), 
							userIds_property_name_list.get(i), userIds_check_same_property_name_list.get(i), null, userIds_compare_date_property_name_list.get(i));
					if(result_temp != null && !result_temp.isEmpty()){
						result_current_step.addAll(result_temp);
					}
					int start = xml.lastIndexOf(nextStartStr);
					if(start != -1){
						int end = xml.lastIndexOf(nextEndStr);
						if(end > start){
							url=xml.substring(start+nextStartStr.length(), end);
							//url = URLDecoder.decode(url,"UTF-8");
							url = url.replaceAll("&amp;", "&");
						}
					}else{
						break;
					}
				}
				result_previous_step = result_current_step;
			}catch(Exception e){
				System.out.println("get user list error: "+e.getMessage());
			}
		}
		result_set.addAll(result_previous_step);
		return new ArrayList<String>(result_set);
		
		/*//获取需要转换用户的id
		String url = ConfigProperties.getUserIds_url();//"https://api15.sapsf.cn/odata/v2/OnboardingCandidateInfo?$select=userId,readyToHire,managerId,kmsUserId,hrManagerId,hired,hireDate,fromExternalATS,candidateId,applicantId";
		
		boolean flag = true;
		int count = 0;
		final int tryTimes = ConfigProperties.getApp_error_try_times();
		String result = null;
		while(flag){
			try{
				result = HttpUtil.getWithBaseAuth(ConfigProperties.getUserIds_username(), ConfigProperties.getUserIds_password(), url);
				flag = false;
			}catch(Exception ee){
				System.out.println("get user list error: "+ee.getMessage()+", try "+(++count)+" times.");
				if(count > tryTimes){
					flag = false;
				}
			}
		}
		
		if(result == null || result.trim().length() < 100){
			System.out.println(result);
		}
		return XmlUtil.parseUserListXml(result);*/
	}
	
	private static String getUserIdsXML(String username,String password,String url){
		boolean flag = true;
		int count = 0;
		final int tryTimes = ConfigProperties.getApp_error_try_times();
		String result = null;
		while(flag){
			try{
				result = HttpUtil.getWithBaseAuth(username, password, url);
				flag = false;
			}catch(Exception ee){
				System.out.println("get user list error: "+ee.getMessage()+", try "+(++count)+" times.");
				if(count > tryTimes){
					flag = false;
				}
			}
		}
		if(result == null || result.trim().length() < 100){
			return null;
		}
		return result;
	}

	/**
	 * Remove Before BackgroundInfo
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	private static void userBackgroundInfDel(List<String> userIds) throws FileNotFoundException, IOException, Exception{
		//获取访问查询人员信息的秘钥（即ticket）并获取该用户的在ONB中的所有信息
		ObjectMapper mapper = null;
		JSONObject resultObj = null;
		List<String> retList = null;
		Map<String,List<String>> iDTypeList = null;
		//配置文件中获取需要执行删除操作background的entity
		String entitys[] = ConfigProperties.getUserIds_delete_entity().split(",");
		for (String kmsUserId : userIds) {
			iDTypeList = UserInfoWsUtil.getKsUserIdInfo(kmsUserId);
			for(String key : iDTypeList.keySet()) {
				retList = getCardTypeAndId(key, mapper, resultObj, retList);
				if (iDTypeList.get(key).size() != 0 && retList.size() != 0) {
					if (!iDTypeList.get(key).retainAll(retList)) {
						//执行删除操作
						delUserBackground(key, entitys);
					}
				}
			}
		}
		
	}
	
	/**
	 * get ID adn type from EC
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private static List<String> getCardTypeAndId (String userId, ObjectMapper mapper, JSONObject resultObj, List<String> retList) throws JsonParseException, JsonMappingException, IOException {
		String entityNms[] = ConfigProperties.getEntity_property().split(",");
		String url = ConfigProperties.getEc_url();
		String ret = "";
		JSONArray ecInfo = null;
		String itms[] = ConfigProperties.getUserIds_EC_check_delete_target_property().split(",");
		for (int index = 0; index < entityNms.length; index++) {
			url = url + entityNms[index] + "?&$format=json&$select=" + ConfigProperties.getUserIds_EC_check_delete_target_property()
				+ "&$filter=personIdExternal%20eq%20'" + userId + "'";
			ret = getUserIdsXML(ConfigProperties.getUserIds_check_transferred_username(), 
						ConfigProperties.getUserIds_check_transferred_password(), url);
			retList = new ArrayList<String>();
			if (ret != null) {
				mapper = new ObjectMapper();
				resultObj = toJson(mapper, resultObj, ret);
				ecInfo = resultObj.getJSONObject("d").getJSONArray("results");
				for (int i = 0; i < ecInfo.length(); i++) {
					for (int j = 0; j < itms.length; j=+2) {
						retList.add(ecInfo.getJSONObject(i).getString(itms[j]) + ecInfo.getJSONObject(i).getString(itms[j+1]));
					}
				}
			}
		}
		
		return retList;
	}
	
	/**
	 * String to JSONObject
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private static JSONObject toJson (ObjectMapper mapper, JSONObject resultObj, String ret) throws JsonParseException, JsonMappingException, IOException {
		mapper = new ObjectMapper();  
		resultObj = new JSONObject(mapper.readValue(ret.toString(), Map.class));
		return resultObj;
	}
	
	/**
	 * get backgroundElementId from background table
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private static void delUserBackground (String userid, String entitys[]) throws JsonParseException, JsonMappingException, IOException {
		String url = null;
		String ret = null;
		ObjectMapper mapper = null;
		JSONObject resultObj = null;
		JSONArray ecInfo = null;
		for (int i = 0; i < entitys.length; i++) {
			url = ConfigProperties.getEc_url() + entitys[i] + "?$format=json&$select=userId,backgroundElementId&$filter=userId%20eq%20'"+ userid + "'";
			ret = getUserIdsXML(ConfigProperties.getUserIds_check_transferred_username(), 
						ConfigProperties.getUserIds_check_transferred_password(), url);
			if (ret != null) {
				resultObj = toJson(mapper, resultObj, ret);
				ecInfo = resultObj.getJSONObject("d").getJSONArray("results");
				for (int j=0; j< ecInfo.length(); j++) {
					url = ConfigProperties.getEc_url() + entitys[i] + "(backgroundElementId=" + ecInfo.getJSONObject(j).getString("backgroundElementId")
					+ "L,userId='" + userid + "')?$format=json";
					del(ConfigProperties.getUserIds_check_transferred_username(), 
								ConfigProperties.getUserIds_check_transferred_password(), url);
				}
			}
		}
	}
	
	/**
	 * delete from background table
	 */
	private static void del(String username,String password,String url){
		boolean flag = true;
		int count = 0;
		final int tryTimes = ConfigProperties.getApp_error_try_times();
		while(flag){
			try{
				HttpUtil.delWithBaseAuth(username, password, url);
				flag = false;
			}catch(Exception ee){
				System.out.println("get user list error: "+ee.getMessage()+", try "+(++count)+" times.");
				if(count > tryTimes){
					flag = false;
				}
			}
		}
	}
}
