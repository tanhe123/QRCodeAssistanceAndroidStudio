package com.sdutlinux.service;

import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;	

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sdutlinux.utils.JsonParser;

public class WebService {
	public static final String BASE_URL = "http://210.44.176.204";
//	public static final String BASE_URL = "http://192.168.1.152:8000";
	public static final String SERVER_URL = BASE_URL + "/devices/phone/";
	public static final String LOGIN_URL = BASE_URL + "/accounts/phone/login/";
	public static final String ISSUE_INFO_URL = BASE_URL + "/maintain/phone/";
	public static final String ISSUE_URL = BASE_URL + "/maintain/phone/send/";
	
	public static final String TAG = "WebServiceTest";
	
	private static String SESSIONID = null;

	private Context context;
	public WebService(Context context) {
		this.context = context;
	}
	
	// 测试用g
	public String post(String url, List<BasicNameValuePair> params) {
		try {
 			HttpPost request = new HttpPost(url); // 根据内容来源地址创建一个Http请求
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); // 设置参数的编码
			
			// 设置sessionID
//			if (null != SESSIONID) {
//				request.setHeader("Cookie", "sessionid=" + SESSIONID);
//			}
			// 为请求添加 sessionid
			setSessionId(request);
			
			DefaultHttpClient client = new DefaultHttpClient();

			// 设置超时时间
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
			
			HttpResponse httpResponse = client.execute(request); // 发送请求并获取反馈
			
			// 解析返回的内容
			if (httpResponse.getStatusLine().getStatusCode() != 404) {
				// 获取 Cookie, sessionid
//				List<Cookie> cookie = client.getCookieStore().getCookies();
				
//				for (Cookie c : cookie) {
//					if (c.getName().equals("sessionid")) {
//						SESSIONID = c.getValue();	
//					}
//				}
				
				// 将请求中的sessionid保存起来
				setSessionId(client);
				
				String result = EntityUtils.toString(httpResponse.getEntity());
				Log.i(TAG, result);
				
				return result;
			}
		} catch (Exception e) {
			Log.i(TAG, "exception");
		}
		return null;
	}
	
	/**
	 * 给 request 添加 sessionid
	 * @param request
	 */
	private void setSessionId(HttpRequest request) {
		// 设置sessionID
		if (null != SESSIONID) {
			request.setHeader("Cookie", "sessionid=" + SESSIONID);
		}		
	}
	
	/**
	 * 将 session 保存起来
	 * @param client
	 * @return
	 */
	public void setSessionId(DefaultHttpClient client) {
		// 获取 Cookie, sessionid
		List<Cookie> cookie = client.getCookieStore().getCookies();
		
		for (Cookie c : cookie) {
			if (c.getName().equals("sessionid")) {
				SESSIONID = c.getValue();	
			}
		}
	}
	
	// 测试用
	public String get(String url) {
		try {
			// 根据内容来源地址创建一个Http请求
			HttpGet request = new HttpGet(url);
			
			setSessionId(request);
			
			DefaultHttpClient client = new DefaultHttpClient();
			
			// 设置参数的编码
			HttpResponse httpResponse = client.execute(request); // 发送请求并获取反馈
			// 解析返回的内容
			if (httpResponse.getStatusLine().getStatusCode() != 404) {
				setSessionId(client);
				
				String result = EntityUtils.toString(httpResponse.getEntity());
				return result;
			}
		} catch (Exception e) {
			Toast.makeText(context, "发生错误", Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	/**
	 * post 方式 获取 json 信息
	 * @param url 存储信息的 url
	 * @return 返回JSONObject对象
	 * @throws JSONException 
	 */
	public JSONObject getJson(String url, List<BasicNameValuePair> params) throws JSONException {
		String content = post(url, params);
		
		JSONObject jsonObj = JsonParser.getJsonFromString(content, "QRinfo");
		
		return jsonObj;
	}
	
	/**
	 * 通过get方式获得json
	 * @return
	 * @throws JSONException 
	 */
	public JSONObject getJson(String url) throws JSONException {
		String content = get(url);
		JSONObject jsonObject = JsonParser.getJsonFromString(content, "QRinfo");
		
		return jsonObject;
	}
}
