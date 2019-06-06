package com.capgemini.transfer.http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	
	public static String getWithBaseAuth(String username, String password, String url) throws ClientProtocolException, IOException{
		HttpClientContext context = HttpClientContext.create();
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
        org.apache.http.auth.Credentials credentials = new org.apache.http.auth.UsernamePasswordCredentials(username, password);
        credsProvider.setCredentials(org.apache.http.auth.AuthScope.ANY, credentials);
        context.setCredentialsProvider(credsProvider);
        
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(url);
		return EntityUtils.toString(httpClient.execute(httpget, context).getEntity(), "utf-8");
	}
	
	public static String delWithBaseAuth(String username, String password, String url) throws ClientProtocolException, IOException{
		HttpClientContext context = HttpClientContext.create();
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
        org.apache.http.auth.Credentials credentials = new org.apache.http.auth.UsernamePasswordCredentials(username, password);
        credsProvider.setCredentials(org.apache.http.auth.AuthScope.ANY, credentials);
        context.setCredentialsProvider(credsProvider);
        
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpDelete httpDelete = new HttpDelete(url);
		return EntityUtils.toString(httpClient.execute(httpDelete, context).getEntity(), "utf-8");
	}
}
