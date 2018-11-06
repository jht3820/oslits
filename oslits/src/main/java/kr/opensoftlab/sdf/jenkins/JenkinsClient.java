package kr.opensoftlab.sdf.jenkins;

import java.io.IOException;
import java.net.URI;





import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JenkinsClient {

	public final static String JENKINS_URL = "http://192.168.0.4:8080/";

	String user = "";
	String password = ""; //api token정보
	
	public JenkinsClient(String user,String password){
		this.user = user;
		this.password = password;
	}
	

	public Map getJenkinsParser(String content) throws ParseException{

		JSONParser jsonParser = new JSONParser();

		//넘겨 받은 데이터 JSON Array로 파싱
		Map jsonMap = (Map) jsonParser.parse(content);

		return jsonMap;

	}


	public String excuteHttpClientJenkins(String url) throws Exception{

		String responseStr = "";

		URI uri = URI.create(url);
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(user, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
	
		HttpGet httpGet = new HttpGet(uri);
		// Add AuthCache to the execution context
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);

		HttpResponse response = httpClient.execute(host, httpGet, localContext);

		HttpEntity entity= response.getEntity();
		
		Header  header=entity.getContentType();
		if(header!=null){
			System.out.println(header.getValue());
		}
		
		
		responseStr = EntityUtils.toString(entity);
		
		EntityUtils.consume(entity);
		
		httpClient.close();
		

		return responseStr;

	}
	
	
	public Map excuteHttpClient(String url) throws Exception{

		String responseStr = "";

		URI uri = URI.create(url);
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(user, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
	
		HttpGet httpGet = new HttpGet(uri);
		// Add AuthCache to the execution context
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);

		HttpResponse response = httpClient.execute(host, httpGet, localContext);
		
				
		HttpEntity entity= response.getEntity();
		
		Header  header=entity.getContentType();
		System.out.println(header.getValue());
		
		
		responseStr = EntityUtils.toString(entity);
		
		EntityUtils.consume(entity);
		
		httpClient.close();
		

		return null;

	}
	
	


}
