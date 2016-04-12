package com.prophecysenorlytic.diag.resthandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientFactory {

	public HttpClientFactory() {
		// TODO Auto-generated constructor stub
	}

	public static HttpClient getClient() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30 * 1000).build();
		HttpClient hc = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
		return hc;
	}

	
	
	
	public static HttpPost buildPost(String url, String contentType, String acceptType) {
		HttpPost post = new HttpPost(url);
		if (null != contentType) {
			post.addHeader("Content-Type", contentType);
		}
		if (null != acceptType) {
			post.addHeader("Accept", acceptType);
		}
//		post.addHeader("X-Access-Token", (String) Sessions.getCurrent().getAttribute(UtilConstants._LOGGED_IN_TOKEN));
		return post;
	}
	
	
	
	public static HttpPost buildPost_Test(String url, String contentType, String acceptType) {
		HttpPost post = new HttpPost(url);
		if (null != contentType) {
			post.addHeader("Content-Type", contentType);
		}
		if (null != acceptType) {
			post.addHeader("Accept", acceptType);
		}
//		post.addHeader("X-Access-Token", (String) Sessions.getCurrent().getAttribute(UtilConstants._LOGGED_IN_TOKEN));
		return post;
	}

	public static HttpGet buildParameterisedGet(String url, String contentType, String acceptType, Object... objects) {
		url = String.format(url, objects);
		HttpGet get = new HttpGet(url);
		if (null != contentType) {
			get.addHeader("Content-Type", contentType);
		}
		if (null != acceptType) {
			get.addHeader("Accept", acceptType);
		}
//		get.addHeader("X-Access-Token", (String) Sessions.getCurrent().getAttribute(UtilConstants._LOGGED_IN_TOKEN));
		return get;
	}

	public static HttpGet buildGet(String url, String contentType, String acceptType) {
		HttpGet get = new HttpGet(url);
		if (null != contentType) {
			get.addHeader("Content-Type", contentType);
		}
		if (null != acceptType) {
			get.addHeader("Accept", acceptType);
		}
//		get.addHeader("X-Access-Token", (String) Sessions.getCurrent().getAttribute(UtilConstants._LOGGED_IN_TOKEN));
		return get;
	}

	public static String readGetResponse(String url, String contentType, String acceptType)
			throws ClientProtocolException, IOException {
		HttpGet get = buildGet(url, contentType, acceptType);
		HttpResponse response = HttpClientFactory.getClient().execute(get);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		response.getEntity().writeTo(bos);
		String data = new String(bos.toByteArray());
		bos.close();

		bos = null;
		get.releaseConnection();
		return data;

	}

	public static HttpPut buildPut(String url, String contentType, String acceptType) {
		HttpPut put = new HttpPut(url);
		if (null != contentType) {
			put.addHeader("Content-Type", contentType);
		}
		if (null != acceptType) {
			put.addHeader("Accept", acceptType);
		}
//		put.addHeader("X-Access-Token", (String) Sessions.getCurrent().getAttribute(UtilConstants._LOGGED_IN_TOKEN));
		return put;
	}

	public static HttpDelete buildDelete(String url, String contentType, String acceptType) {
		HttpDelete del = new HttpDelete(url);
		if (null != contentType) {
			del.addHeader("Content-Type", contentType);
		}
		if (null != acceptType) {
			del.addHeader("Accept", acceptType);
		}

		// del.addHeader("X-Access-Token", (String)
		// Sessions.getCurrent().getAttribute(UtilConstants._LOGGED_IN_TOKEN));
		return del;
	}
}
