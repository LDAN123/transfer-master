package com.capgemini.transfer.ws.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.dom4j.DocumentException;

import com.capgemini.transfer.config.Config.Ref;
import com.capgemini.transfer.config.ConfigProperties;
import com.capgemini.transfer.xml.XmlUtil;

public class UserInfoWsUtil {
	private static final QName SERVICE_NAME = new QName("http://ATS.online-onboarding.com/Client/HRDataServiceEx", "RequestResponseHRDataService");
	
	public static List<String[]> getUserInfoXml(List<String> userIds, List<Ref> refs, String username, String password, List<String> transferredUserIds) throws DocumentException{
		URL wsdlURL = null;
		String ws_url = ConfigProperties.getWs_url() == null ? "https://onboarding15.sapsf.cn/ONBPREM/Services/HRDataServiceEx.asmx?WSDL" : ConfigProperties.getWs_url();
		try {
			wsdlURL = new URL(ws_url);
        } catch (MalformedURLException e) {
        	System.out.println("Can not initialize the default wsdl from "+ws_url);
        	return null;
        }
        RequestResponseHRDataService ss = new RequestResponseHRDataService(wsdlURL, SERVICE_NAME);
        RequestResponseHRDataBinding port = ss.getRequestResponseHRDataBinding();
        
        java.lang.String _beginSession__return = port.beginSession(username, password);
        String ticket_tag = "<Ticket>";
        String ticket_end_tag = "</Ticket>";
        int start = _beginSession__return.indexOf(ticket_tag)+ticket_tag.length();
        int end = _beginSession__return.indexOf(ticket_end_tag, start);
        String ticket = _beginSession__return.substring(start, end);
        
        List<String[]> result = new ArrayList<String[]>();
        String xml = null;
        List<String[]> temp = null;
        ticket_tag = "<history>";
        ticket_end_tag = "</history>";
        //System.out.println(transferredUserIds);
        //System.out.println(ConfigProperties.getUserIds_check_transferred_key());
        List<String> needRemoveUserIds = new ArrayList<String>();
        for(String userId : userIds){
        	System.out.println("start get user info with ws, user id: "+userId);
            xml = port.getNewhireRecord(ticket, userId);
            System.out.println(xml);
            if(xml == null || xml.isEmpty()){
            	continue;
            }
            if(isTransferredUser(xml,transferredUserIds)){
            	needRemoveUserIds.add(userId);
            	System.out.println("--------------------------------------------------user transferred, user id: "+userId);
            	continue;
            }
            while(true){
            	start = xml.indexOf(ticket_tag);
            	end = xml.indexOf(ticket_end_tag);
            	if(start == -1){
            		break;
            	}
            	xml = xml.substring(0, start)+xml.substring(end+ticket_end_tag.length());
            }
            System.out.println(xml);
            temp = XmlUtil.parseUserListXml(xml, refs);
            //System.out.println(temp);
            System.out.println("end get user info with ws, user id: "+userId+", info rows: "+(temp == null ? 0 : temp.size()));
            if(temp != null && !temp.isEmpty()){
            	result.addAll(temp);
            }
        }
        userIds.removeAll(needRemoveUserIds);
		return result;
	}
	private static boolean 	isTransferredUser(String userInfoXML, List<String> transferredUserIds){
		try{
			String[] userIds_check_transferred_keys = ConfigProperties.getUserIds_check_transferred_key() == null ? new String[]{} : ConfigProperties.getUserIds_check_transferred_key().split(",");
			int keyStart = 0;
			for(String key : userIds_check_transferred_keys){
				//System.out.print("key:"+key);
	        	if(key != null && !key.trim().isEmpty() && (keyStart = userInfoXML.indexOf("<key>"+key+"</key>")) != -1){
	        		int start = keyStart + ("<key>"+key+"</key>").length();
	        		start = userInfoXML.indexOf("<value>", start);
	        		int end = userInfoXML.indexOf("</value>", keyStart);
	        		if(start == -1 || end == -1){
	        			continue;
	        		}
	        		start += "<value>".length();
	        		String value = userInfoXML.substring(start, end);
	        		//System.out.print(" value:"+value);
	        		for(String transferredUserId : transferredUserIds){
	        			if(transferredUserId != null && transferredUserId.equals(value.trim())){
	        				System.out.println("user transferred, "+key+": "+transferredUserId);
	        				return true;
	        			}
	        		}
	        	}
	        	//System.out.println();
	        }
		}catch(Exception e){}
		return false;
	}
	
	//获取访问查询人员信息的秘钥
	@SuppressWarnings({ "null", "unused" })
	public static Map<String, List<String>> getKsUserIdInfo(String kmsUserId) throws DocumentException{
		URL wsdlURL = null;
		String ws_url = ConfigProperties.getWs_url() == null ? "https://onboarding15.sapsf.cn/ONBPREM/Services/HRDataServiceEx.asmx?WSDL" : ConfigProperties.getWs_url();
		try {
			wsdlURL = new URL(ws_url);
        } catch (MalformedURLException e) {
        	System.out.println("Can not initialize the default wsdl from "+ws_url);
        	return null;
        }
        RequestResponseHRDataService ss = new RequestResponseHRDataService(wsdlURL, SERVICE_NAME);
        RequestResponseHRDataBinding port = ss.getRequestResponseHRDataBinding();
        
        java.lang.String _beginSession__return = port.beginSession(ConfigProperties.getWs_username(), ConfigProperties.getWs_password());
        String ticket_tag = "<Ticket>";
        String ticket_end_tag = "</Ticket>";
        int start = _beginSession__return.indexOf(ticket_tag)+ticket_tag.length();
        int end = _beginSession__return.indexOf(ticket_end_tag, start);
        String ticket = _beginSession__return.substring(start, end);
        
        String xml = null;
        String keys[] = ConfigProperties.getUserIds_ONB_check_delete_target_property().split(",");
    	//该用户的在ONB中的所有信息
        xml = port.getNewhireRecord(ticket, kmsUserId);
        if(xml == null || xml.isEmpty()){
        	return null;
        }
        List<String> value = new ArrayList<String>();
        String iDvalueXml = null;
        String typevalueXml = null;
        for (int i= 0; i < keys.length; i=i+2) {
        	iDvalueXml = XmlUtil.parseValueXml(xml, keys[i]);
        	typevalueXml = XmlUtil.parseValueXml(xml, keys[i+1]);
        	if ((iDvalueXml != null && !typevalueXml.isEmpty())
        			|| (typevalueXml != null && !typevalueXml.isEmpty())) {
        		value.add(iDvalueXml + typevalueXml);
        	}
        	
        }
        Map<String, List<String>> ret = new HashMap<String, List<String>>();
        if (value != null) {
        	ret.put(XmlUtil.parseValueXml(xml, "EmployeeLogin"), value);
        }
        
		return ret;
	}
}
