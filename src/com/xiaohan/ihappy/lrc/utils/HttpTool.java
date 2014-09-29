package com.xiaohan.ihappy.lrc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpTool {
	public static final int GET = 1;
	public static final int POST = 2;
	
	public static long getLength(String path,
			ArrayList<BasicNameValuePair> headers,
			ArrayList<BasicNameValuePair> params, int method)
			throws IOException {
		long length = 0;
		HttpEntity entity = getEntity(path, headers, params, method);
		if (entity != null) {
			length = entity.getContentLength();
		}
		return length;
	}

	public static InputStream getStream(String path,
			ArrayList<BasicNameValuePair> headers,
			ArrayList<BasicNameValuePair> params, int method)
			throws IOException {
		InputStream in = null;
		HttpEntity entity = getEntity(path, headers, params, method);
		if (entity != null) {
			in = entity.getContent();
		}
		return in;
	}

	public static byte[] getBytes(String path,
			ArrayList<BasicNameValuePair> headers,
			ArrayList<BasicNameValuePair> params, int method)
			throws IOException {
		byte[] bytes = null;
		HttpEntity entity = getEntity(path, headers, params, method);
		if (entity != null) {
			bytes = EntityUtils.toByteArray(entity);
		}
		return bytes;
	}

	@SuppressWarnings("deprecation")
	private static HttpEntity getEntity(String path,
			ArrayList<BasicNameValuePair> headers,
			ArrayList<BasicNameValuePair> params, int method)
			throws IOException {
		HttpEntity entity = null;
		// ����client
		DefaultHttpClient client = new DefaultHttpClient();
		// ����request����
		HttpUriRequest request = null;
		switch (method) {
		case GET:
			StringBuilder sb = new StringBuilder(path);
			// ����в������򽫲������Ͳ���ֵ���ӵ�path֮��
			if (params != null && !params.isEmpty()) {
				sb.append('?');
				for (BasicNameValuePair pair : params) {
					sb.append(pair.getName()).append('=')
							.append(URLEncoder.encode(pair.getValue()))
							.append('&');
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			request = new HttpGet(sb.toString());
			break;
		case POST:
			request = new HttpPost(path);
			if (params != null && !params.isEmpty()) {
				// ����post���������ʵ��
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
				((HttpPost) request).setEntity(ent);
			}
			break;
		}
		// �����Ϣͷ������Ϊ�գ���ѭ���������е���Ϣͷ
		if (headers != null && !headers.isEmpty()) {
			for (BasicNameValuePair pair : headers) {
				request.setHeader(pair.getName(), pair.getValue());
			}
		}
		// ִ�����󣬻��response
		HttpResponse response = client.execute(request);
		// �ж�response״̬��
		int code = response.getStatusLine().getStatusCode();
		if (code == HttpStatus.SC_OK) {
			// �������ɹ��������Ӧʵ��
			entity = response.getEntity();
		}

		return entity;
	}

	@SuppressWarnings("deprecation")
	public static String getresponse(String path,
			ArrayList<BasicNameValuePair> headers,
			ArrayList<BasicNameValuePair> params, int method) {

		
		// ����client
		DefaultHttpClient client = new DefaultHttpClient();
		// ����request����
		HttpUriRequest request = null;
		switch (method) {
		case GET:
			StringBuilder sb = new StringBuilder(path);
			// ����в������򽫲������Ͳ���ֵ���ӵ�path֮��
			if (params != null && !params.isEmpty()) {
				sb.append('?');
				for (BasicNameValuePair pair : params) {
					sb.append(pair.getName()).append('=')
							.append(URLEncoder.encode(pair.getValue()))
							.append('&');
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			request = new HttpGet(sb.toString());
			break;
		case POST:
			request = new HttpPost(path);
			if (params != null && !params.isEmpty()) {
				// ����post���������ʵ��
				UrlEncodedFormEntity ent = null;
				try {
					ent = new UrlEncodedFormEntity(params);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				((HttpPost) request).setEntity(ent);
			}
			break;
		}
		// �����Ϣͷ������Ϊ�գ���ѭ���������е���Ϣͷ
		if (headers != null && !headers.isEmpty()) {
			for (BasicNameValuePair pair : headers) {
				request.setHeader(pair.getName(), pair.getValue());
			}
		}
		// ִ�����󣬻��response
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.toString();

	}

}
