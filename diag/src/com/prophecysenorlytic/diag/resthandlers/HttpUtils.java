package com.prophecysenorlytic.diag.resthandlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

public class HttpUtils {

	public static HttpPost buildPost(String url, String contentType, String acceptType) {
		HttpPost post = new HttpPost(url);
		if (null != contentType) {
			post.addHeader("Content-Type", contentType);
		}
		if (null != acceptType) {
			post.addHeader("Accept", acceptType);
		}

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
		// post.addHeader("X-Access-Token", (String)
		// Sessions.getCurrent().getAttribute(UtilConstants._LOGGED_IN_TOKEN));
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

	public static String readPostResponse(String url, String contentType, String acceptType, String json) {
		String data = null;
		HttpPost post = null;
		try {
			post = buildPost(url, contentType, acceptType);
			post.setEntity(new StringEntity(json));
			HttpResponse response = HttpClientFactory.getClient().execute(post);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			response.getEntity().writeTo(bos);
			data = new String(bos.toByteArray());
			bos.close();

			bos = null;

			return data;
		} catch (Throwable t) {
			t.printStackTrace();

		} finally {
			post.releaseConnection();
		}
		return data;

	}

	public static String readPutResponse(String url, String contentType, String acceptType) {
		String data = null;
		HttpPut put = null;
		try {
			put = buildPut(url, contentType, acceptType,false);
			HttpResponse response = HttpClientFactory.getClient().execute(put);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			response.getEntity().writeTo(bos);
			data = new String(bos.toByteArray());
			bos.close();

			bos = null;

			return data;
		} catch (Throwable t) {
			t.printStackTrace();

		} finally {
			put.releaseConnection();
		}
		return data;

	}

	public static String readDeleteResponse(String url, String contentType, String acceptType) {
		String data = null;
		HttpDelete delete = null;
		try {
			delete = buildDelete(url, contentType, acceptType);
			HttpResponse response = HttpClientFactory.getClient().execute(delete);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			response.getEntity().writeTo(bos);
			data = new String(bos.toByteArray());
			bos.close();

			bos = null;

			return data;
		} catch (Throwable t) {
			t.printStackTrace();

		} finally {
			delete.releaseConnection();
		}
		return data;

	}

	public static HttpPut buildPut(String url, String contentType, String acceptType, boolean shouldUseToken) {
		HttpPut put = new HttpPut(url);
		if (null != contentType) {
			put.addHeader("Content-Type", contentType);
		}
		if (null != acceptType) {
			put.addHeader("Accept", acceptType);
		}
		/*if (shouldUseToken) {
			put.addHeader("X-Access-Token",
					(String) Sessions.getCurrent().getAttribute(UtilConstants._LOGGED_IN_TOKEN));
		}*/
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

	public static String readPutResponse(String url, String contentType, String acceptType, String payload) {
		// TODO Auto-generated method stub
		String data = null;
		HttpPut put = null;
		try {
			put = buildPut(url, contentType, acceptType,false);
			put.setEntity(new StringEntity(payload));
			HttpResponse response = HttpClientFactory.getClient().execute(put);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			response.getEntity().writeTo(bos);
			data = new String(bos.toByteArray());
			bos.close();

			bos = null;

			return data;
		} catch (Throwable t) {
			t.printStackTrace();

		} finally {
			put.releaseConnection();
		}
		return data;

	}

}
