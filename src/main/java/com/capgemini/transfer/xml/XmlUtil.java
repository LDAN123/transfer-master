package com.capgemini.transfer.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.capgemini.transfer.config.Config.Ref;
import com.capgemini.transfer.config.ConfigProperties;

public class XmlUtil {
	public static final String USER_INFO_XPATH = "//m:properties";
	public static final String HIRE_DATE_TAG = "hireDate";
	public static final String USER_ID_TAG = "kmsUserId";
	public static final String EMPLOYEE_LOGIN = "EmployeeLogin";
	public static final DateFormat SF = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	//public static final int DAYS_NUM = 3;

	//public static final String USER_INFO_ITEM_XPATH = "/GetNewhireRecordResult/NewhireRecord/DictionarySerializer/dictionary/item";
	public static final String USER_INFO_TAG_NAME = "dictionary";

	public static List<String> parseUserListXml(String xml) throws DocumentException {
		return parseUserListXml(xml, null, null, null);
	}

	public static List<String> parseUserListXml(String xml, String userInfoPath) throws DocumentException {
		return parseUserListXml(xml, null, null, userInfoPath);
	}

	public static List<String> parseUserListXml(String xml, String hireDateTag, String userIdTag)
			throws DocumentException {
		return parseUserListXml(xml, hireDateTag, userIdTag, null);
	}

	//解析需要处理的用户ID
	@SuppressWarnings("unchecked")
	public static List<String> parseUserListXml(String xml, String hireDateTag, String userIdTag, String userInfoPath)
			throws DocumentException {
		if (xml == null || xml.trim().isEmpty()) {
			return null;
		}
		if (hireDateTag == null || hireDateTag.trim().isEmpty()) {
			hireDateTag = HIRE_DATE_TAG;
		}
		if (userIdTag == null || userIdTag.trim().isEmpty()) {
			userIdTag = USER_ID_TAG;
		}
		if (userInfoPath == null || userInfoPath.trim().isEmpty()) {
			userInfoPath = USER_INFO_XPATH;
		}
		InputStream f = new ByteArrayInputStream(xml.getBytes());
		SAXReader reader = new SAXReader();
		Document doc = reader.read(f);
		// user info xpath
		List<Element> list = doc.selectNodes(userInfoPath);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<String> result = new ArrayList<String>(list.size());
		Element e = null;
		Element sub_e = null;
		String hireDate = null;
		String userId = null;
		Date today = clearTime(new Date());
		Date today_before_3_days = DateUtils.addDays(today, -ConfigProperties.getUserIds_filter_day_num());
		Date today_after_3_days = DateUtils.addDays(today, ConfigProperties.getUserIds_filter_day_num());
		for (Iterator<Element> i = list.iterator(); i.hasNext();) {
			e = i.next();
			hireDate = null;
			userId = null;
			// read hireDate and userId
			for (Iterator<Element> sub_i = e.elementIterator(); sub_i.hasNext();) {
				sub_e = sub_i.next();
				// System.out.println(sub_e.getName());
				if (hireDateTag.equalsIgnoreCase(sub_e.getName())) {
					hireDate = sub_e.getTextTrim();
				}
				if (userIdTag.equalsIgnoreCase(sub_e.getName())) {
					userId = sub_e.getTextTrim();
				}
			}
			if (hireDate != null && !hireDate.trim().isEmpty() && userId != null && !userId.trim().isEmpty()) {
				try {
					Date hd = SF.parse(hireDate);
					if (DateUtils.isSameDay(hd, today_after_3_days) || DateUtils.isSameDay(hd, today_before_3_days) ||
							(hd.before(today_after_3_days) && hd.after(today_before_3_days))) {
						result.add(userId);
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
					continue;
				}
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public static List<String> parseUserListXml(String xml,List<String> needFilterUserId,String checkDatePropertyName,Integer dayNumAroundtoday,
			String getPropertyName,String checkSamePropertyName,String userInfoPath, String userIds_compare_date_property_name)throws DocumentException {
		if (xml == null || xml.trim().isEmpty()) {
			return null;
		}
		if(checkSamePropertyName != null && !checkSamePropertyName.trim().isEmpty()){//需要筛选
			if(needFilterUserId == null || needFilterUserId.isEmpty()){//筛选数据集为空，返回null
				return null;
			}
		}
		if (getPropertyName == null || getPropertyName.trim().isEmpty()) {
			getPropertyName = USER_ID_TAG;
		}
		if (userInfoPath == null || userInfoPath.trim().isEmpty()) {
			userInfoPath = USER_INFO_XPATH;
		}
		InputStream f = new ByteArrayInputStream(xml.getBytes());
		SAXReader reader = new SAXReader();
		Document doc = reader.read(f);
		// user info xpath
		List<Element> list = doc.selectNodes(userInfoPath);
		if (list == null || list.isEmpty()) {
			return null;
		}
		Set<String> user_ids_set = new HashSet<String>();
		Element e = null;
		Element sub_e = null;
		String hireDate = null;
		String userId = null;
		String userIdFilterValue = null;
		String compareDate = null;
		Map<String, String> checkUserIdMap = new HashMap<String, String>();
		Map<String, String> checkKmsUserIdMap = new HashMap<String, String>();
		for (Iterator<Element> i = list.iterator(); i.hasNext();) {
			e = i.next();
			hireDate = null;
			userId = null;
			// read hireDate and userId
			for (Iterator<Element> sub_i = e.elementIterator(); sub_i.hasNext();) {
				sub_e = sub_i.next();
				// System.out.println(sub_e.getName());
				if (checkDatePropertyName != null && !checkDatePropertyName.trim().isEmpty() && checkDatePropertyName.equalsIgnoreCase(sub_e.getName())) {
					hireDate = sub_e.getTextTrim();
				}
				// System.out.println(sub_e.getName());
				if (userIds_compare_date_property_name != null && !userIds_compare_date_property_name.trim().isEmpty() 
						&& userIds_compare_date_property_name.equalsIgnoreCase(sub_e.getName())) {
					compareDate = sub_e.getTextTrim();
				}
				if (checkSamePropertyName != null && !checkSamePropertyName.trim().isEmpty() && checkSamePropertyName.equalsIgnoreCase(sub_e.getName())) {
					userIdFilterValue = sub_e.getTextTrim();
				}
				if (getPropertyName.equalsIgnoreCase(sub_e.getName())) {
					userId = sub_e.getTextTrim();
				}
			}
			//System.out.println(userIdFilterValue);
			if(userId == null || userId.trim().isEmpty()){
				continue;
			}
			if(checkSamePropertyName != null && !checkSamePropertyName.trim().isEmpty()){//需要筛选
				if(needFilterUserId != null && !needFilterUserId.contains(userIdFilterValue)){
					continue;
				}
			}
			if (hireDate != null && !hireDate.trim().isEmpty()) {
				try {
					if(dayNumAroundtoday != null){
						if(isInnerDaysWithToday(hireDate, dayNumAroundtoday)){
							user_ids_set.add(userId);
						}else{
							continue;
						}
					}
					user_ids_set.add(userId);
				} catch (Exception e1) {
					e1.printStackTrace();
					continue;
				}
			}else{
				try {
					if (userIds_compare_date_property_name != null && !userIds_compare_date_property_name.trim().isEmpty()) {
						if (checkUserIdMap.containsKey(userIdFilterValue)) {
							if (dateFormat.parse(checkUserIdMap.get(userIdFilterValue))
									.compareTo(dateFormat.parse(compareDate)) < 0) {
								user_ids_set.remove(checkKmsUserIdMap.get(userIdFilterValue));
								user_ids_set.add(userId);
							}
						} else {
							user_ids_set.add(userId);
							checkUserIdMap.put(userIdFilterValue, compareDate);
							checkKmsUserIdMap.put(userIdFilterValue, userId);
						}
					} else {
						user_ids_set.add(userId);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					continue;
				}
			}
		}
//		e = doc.getRootElement();
//		list = e.elements("link");
//		if(list == null || list.isEmpty()){
//			list = doc.selectNodes("feed/link");
//		}
//		if(list == null || list.isEmpty()){
//			list = doc.selectNodes("//link");
//		}
//		for (Iterator<Element> i = list.iterator(); i.hasNext();) {
//			e = i.next();
//			String next = e.attributeValue("rel");
//			String url = e.attributeValue("href");
//			if("next".equals(next) && url != null){
//				System.out.println(url);
//			}
//		}
		return new ArrayList<String>(user_ids_set);
	}
	private static final DateFormat[] DFS = new DateFormat[]{new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),new SimpleDateFormat("yyyy-MM-dd")};
	//检测给定日期字符串是否在当天的给定天数范围内
	private static boolean isInnerDaysWithToday(String dateStr,Integer dayNumAroundtoday){
		if(dayNumAroundtoday == null){
			return true;
		}
		if(dateStr == null){
			return false;
		}
		Date today = clearTime(new Date());
		Date needCheckDate = null;
		for(DateFormat df : DFS){
			try {
				needCheckDate = df.parse(dateStr);
				if(needCheckDate != null){
					needCheckDate = clearTime(needCheckDate);
					break;
				}
			} catch (ParseException e2) {
				e2.printStackTrace();
			}
		}
		if(needCheckDate == null){
			return false;
		}
		
		dayNumAroundtoday = Math.abs(dayNumAroundtoday);
		Date today_before_3_days = DateUtils.addDays(today, -dayNumAroundtoday);
		Date today_after_3_days = DateUtils.addDays(today, dayNumAroundtoday);
		if (DateUtils.isSameDay(needCheckDate, today_after_3_days) || DateUtils.isSameDay(needCheckDate, today_before_3_days) ||
				(needCheckDate.before(today_after_3_days) && needCheckDate.after(today_before_3_days))) {
			return true;
		}
		return false;
	}
	private static Date clearTime(Date date){
		date = DateUtils.setHours(date, 0);
		date = DateUtils.setMinutes(date, 0);
		date = DateUtils.setSeconds(date, 0);
		date = DateUtils.setMilliseconds(date, 0);
		return date;
	}

	//根据配置解析用户个人信息
	@SuppressWarnings("unchecked")
	public static List<String[]> parseUserListXml(String xml, List<Ref> refs/*String[] equalKeys, String[] likeKeys*/)
			throws DocumentException {
		if (xml == null || xml.trim().isEmpty()) {
			return null;
		}
		if (refs == null || refs.isEmpty()) {
			return null;
		}
		InputStream f = null;
		try {
			f = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SAXReader reader = new SAXReader();
		Document doc = reader.read(f);
		Element dictionary = getElement(doc.getRootElement(),USER_INFO_TAG_NAME);
		if(dictionary == null){
			return null;
		}
		int keyLength = refs.size();
		String[] values = null;
		Element item = null;
		String[] equalvalues = new String[keyLength];
		int num = 0;
		String nameOfOBN = null;
		for (Iterator<Element> iterator = dictionary.elementIterator(); iterator.hasNext();) {
			item = iterator.next();
			num++;
			for (int i = 0; i < refs.size(); i++) {
				nameOfOBN = refs.get(i).getNameOfOBN();
				if (refs.get(i).isNotRepeat() &&  nameOfOBN != null && !nameOfOBN.trim().isEmpty()
						&& nameOfOBN.equalsIgnoreCase(item.element("key").getTextTrim())) {
					equalvalues[i] = item.element("value").getTextTrim();
				}
			}
		}
		
		List<String[]> result = new ArrayList<String[]>(num);
		values = new String[keyLength+1];
		System.arraycopy(equalvalues, 0, values, 0, equalvalues.length);
		//values[keyLength] = String.valueOf(isEnglishOrChinese);
		result.add(values);
		for(int i=1;i<num;i++){
			result.add(null);
		}
		String key = null;
		String regex = "";
		Pattern pattern = null;
		Matcher matcher = null;
		String numStr = null;
		
		String temp = ConfigProperties.getUser_info_english_chinese_ONB_field();//判断中英文的字段
		String[] fields_english_check = new String[0];
		if(temp != null && !temp.trim().isEmpty()){
			fields_english_check = temp.trim().split(",");
		}
		List<Pattern> english_check_pattern = new ArrayList<Pattern>();
		for(String field : fields_english_check){
			if(field != null && !field.trim().isEmpty()){
				english_check_pattern.add(Pattern.compile("^("+field.trim().toUpperCase()+"[_]{0,1})(\\d{0,3})$"));
			}
		}
		Map<Integer,Boolean> rowIsEnglishOrChinese = new HashMap<Integer,Boolean>();
		int firstNum = -1;
		//判断每行数据的中英文，并查找第一个可明确判断中英文的行
		for (Iterator<Element> iterator_s = dictionary.elementIterator(); iterator_s.hasNext();) {
			item = iterator_s.next();
			key = item.element("key").getTextTrim().toUpperCase();
			for(Pattern english_field_pattern : english_check_pattern){
				matcher = english_field_pattern.matcher(key);
				if(matcher.find()){//
					numStr = matcher.group(2);
					if(numStr == null || numStr.isEmpty()){
						numStr = "0";
					}
					num = Integer.parseInt(numStr);
					//判断是中文/英文，只有都符合英文正则表达式时，才为true
					if(item.element("value") != null && item.element("value").getTextTrim() != null){
						if(firstNum == -1 || firstNum > num){
							firstNum = num;
						}
						if(!item.element("value").getTextTrim().matches(ConfigProperties.getUser_info_english_pattern())){
							rowIsEnglishOrChinese.put(num, Boolean.FALSE);
						}else{
							if(rowIsEnglishOrChinese.get(num) == null){
								rowIsEnglishOrChinese.put(num, Boolean.TRUE);
							}
						}
						//System.out.println(english_field_pattern.pattern()+"---"+key+"---"+item.element("value").getTextTrim()
						//		+"---"+values[keyLength]);
					}
					//System.out.println(likeKeys[i] + "---" + key + "---" + values[i + equalKeys.length]);
					//System.out.println(Arrays.toString(values));
				}
			}
		}
		boolean isEnglishOrChineseDefault = Boolean.TRUE;
		if(firstNum != -1 && rowIsEnglishOrChinese.get(firstNum) != null){
			isEnglishOrChineseDefault = rowIsEnglishOrChinese.get(firstNum);
		}
		
		for (int i = 0; i < refs.size(); i++) {
			nameOfOBN = refs.get(i).getNameOfOBN();
			if (refs.get(i).isRepeat() &&  nameOfOBN!= null && !nameOfOBN.trim().isEmpty()){
				regex = "^("+nameOfOBN.toUpperCase()+"[_]{0,1})(\\d{0,3})$";
				pattern = Pattern.compile(regex);
				for (Iterator<Element> iterator_s = dictionary.elementIterator(); iterator_s.hasNext();) {
					item = iterator_s.next();
					key = item.element("key").getTextTrim().toUpperCase();
//					System.out.println("key: "+item.element("key").getTextTrim()+", value: "+item.element("value").getTextTrim()+", en_key: "+fields_english_check[0]);
					matcher = pattern.matcher(key);
					if(matcher.find()){//
						numStr = matcher.group(2);
						if(numStr == null || numStr.isEmpty()){
							numStr = "0";
						}
						num = Integer.parseInt(numStr);
						
						values = result.get(num);
						if(values == null){
							values = new String[keyLength+1];
							//values[keyLength] = String.valueOf(isEnglishOrChinese);
							System.arraycopy(equalvalues, 0, values, 0, equalvalues.length);
							result.set(num, values);
						}
						values[i] = item.element("value").getTextTrim();
//						System.out.println(nameOfOBN+"---"+key + "---" + values[i]);
//						System.out.println(Arrays.toString(values));
					}
					
				}
			}
		}
		//设置中英文状态
		for(int i=0;i<result.size();i++){
			values = result.get(i);
			if(values != null){
				if(rowIsEnglishOrChinese.get(i) != null){
					values[keyLength] = String.valueOf(rowIsEnglishOrChinese.get(i));
				}else{
					values[keyLength] = String.valueOf(isEnglishOrChineseDefault);
				}
			}
		}
		result.removeAll(Collections.singleton(null));
		for(Iterator<String[]> iterator = result.iterator(); iterator.hasNext();){
			String[] ss = iterator.next();
			boolean remove = true;
			for(int i=0;i<keyLength;i++){
				if(ss[i] != null && !ss[i].trim().isEmpty()){
					remove = false;
					break;
				}
			}
			if(remove){
				iterator.remove();
			}else{
				remove = true;
				for(int i=0;i<keyLength;i++){
					if(((equalvalues[i] == null || equalvalues[i].trim().isEmpty()) && (ss[i] == null || ss[i].trim().isEmpty()))//同时为空
							|| (equalvalues[i] != null && ss[i] != null && equalvalues[i].trim().equals(ss[i].trim()))//不为空且相等
							){
					}else{
						remove = false;
						break;
					}
				}
				if(remove){
					iterator.remove();
				}
			}
		}
		
		boolean isEnglish = true;//是否为英文
		for(Iterator<String[]> iterator = result.iterator(); iterator.hasNext();){
			String[] ss = iterator.next();
			for (int i = 0; i < refs.size(); i++) {
				if(refs.get(i).isStatic()){
					ss[i] = refs.get(i).getNameOfOBN();
				}
//				for(String field : fields){
//					if(field.trim().equalsIgnoreCase(refs.get(i).getNameOfOBN())){
//						if(ss[i] != null && !ss[i].trim().matches(ConfigProperties.getUser_info_english_pattern())){
//							isEnglish = false;
//						}
//					}
//				}
			}
			try{
				isEnglish = Boolean.parseBoolean(ss[keyLength]);
			}catch(Exception e){}
			for (int i = 0; i < refs.size(); i++) {
				if(refs.get(i).isDateType()){//是日期
					if(ss[i] != null && !ss[i].trim().isEmpty()){
						Date date = null;
						try{
							if(refs.get(i).getDf() != null)
								date = refs.get(i).getDf().parse(ss[i]);
						}catch(Exception e){}
						if(date == null){
							date = ConfigProperties.parseUserInfoDate(ss[i]);
						}
						if(date != null){
							try{
								if(isEnglish){
									String str_temp = ConfigProperties.formatEnglishDate(date);
									if(str_temp != null){
										ss[i] = str_temp;
									}
								}else{
									String str_temp = ConfigProperties.formatChineseDate(date);
									if(str_temp != null){
										ss[i] = str_temp;
									}
								}
							}catch(Exception e){}
						}
					}
				}
			}
		}
		return result;
	}
	//解析已传输过用户ID
	@SuppressWarnings("unchecked")
	public static List<String> parseTransferredUserListXml(String xml,List<String> userIds) throws DocumentException, UnsupportedEncodingException{
		if (xml == null || xml.trim().isEmpty()) {
			return null;
		}
		System.out.println(xml);
		InputStream f = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		SAXReader reader = new SAXReader();
		Document doc = reader.read(f);
		List<Element> list = doc.selectNodes(USER_INFO_XPATH);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		String userIdTagName = ConfigProperties.getUserIds_check_transferred_target_userId();//"userId";
		if(userIdTagName == null)
			userIdTagName = "userId";
		String transferrdTag = ConfigProperties.getUserIds_check_transferred_target_property();//"degree";
		if(transferrdTag == null)
			transferrdTag = "degree";
		Element e = null;
		Element sub_e = null;
		String transferredUserId = null;
		boolean transferred = true;
		Set<String> set = new HashSet<String>();
		for (Iterator<Element> i = list.iterator(); i.hasNext();) {
			e = i.next();
			transferred = false;
			transferredUserId = null;
			for (Iterator<Element> sub_i = e.elementIterator(); sub_i.hasNext();) {
				sub_e = sub_i.next();
				System.out.println(sub_e.getName());
				if (transferrdTag.equalsIgnoreCase(sub_e.getName())) {
					System.out.println(sub_e.getTextTrim());
					if(sub_e.getTextTrim() != null && !sub_e.getTextTrim().isEmpty()){
						transferred = true;
					};
				}
				if (userIdTagName.equalsIgnoreCase(sub_e.getName())) {
					transferredUserId = sub_e.getTextTrim();
				}
			}
			if(transferred && transferredUserId != null && !transferredUserId.isEmpty()){
				//result.add(transferredUserId);
				set.add(transferredUserId);
				//userIds.removeAll(result);
				//result.clear();
			}
		}
		//userIds.removeAll(result);
		List<String> result = new ArrayList<String>(set);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Element getElement(Element e,String name) {
		if(e.getName().equals(name)){
			return e;
		}
		//System.out.println(e.asXML());
		for (Iterator<Element> i = e.elementIterator(); i.hasNext();) {
			return getElement(i.next(), name);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static String parseValueXml(String xml, String key) throws DocumentException {
		
		if (xml == null || xml.trim().isEmpty()) {
			return null;
		}
		InputStream f = null;
		try {
			f = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SAXReader reader = new SAXReader();
		Document doc = reader.read(f);
		Element dictionary = getElement(doc.getRootElement(),USER_INFO_TAG_NAME);
		if(dictionary == null){
			return null;
		}
		String value = null;
		Element item = null;
		for (Iterator<Element> iterator = dictionary.elementIterator(); iterator.hasNext();) {
			item = iterator.next();
			if (key.equals(item.element("key").getTextTrim())) {
				value = item.element("value").getTextTrim();
				break;
			}
		}
		
		return value;
	}

}
