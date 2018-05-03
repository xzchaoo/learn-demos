package com.xzchaoo.learn.nanohttpd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPObject;

import java.util.List;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 * Created by Administrator on 2017/4/24.
 */
public class RespUtils {
	public static String MIME_APPLICATION_JSON = "application/json";
	public static String MIME_APPLICATION_JAVASCRIPT = "application/javascript";
	public static final NanoHTTPD.Response BAD_REQUEST;
	public static final boolean CORS = true;
	public final static String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS, HEAD";

	private static NanoHTTPD.Response corsResponse() {
		NanoHTTPD.Response resp = newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, null, 0);
		resp.addHeader("Access-Control-Allow-Origin", "*");
		//resp.addHeader("Access-Control-Allow-Headers", calculateAllowHeaders(queryHeaders));
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.addHeader("Access-Control-Allow-Methods", ALLOWED_METHODS);
		resp.addHeader("Access-Control-Max-Age", "86400");
		resp.closeConnection(true);
		return resp;
	}

	public static NanoHTTPD.Response CORS_RESPONSE = corsResponse();

	static {
		BAD_REQUEST = newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, null, null);
		BAD_REQUEST.closeConnection(true);
	}

	public static NanoHTTPD.Response jsonp(String function, Object target) {
		JSONPObject jsonp = new JSONPObject(function);
		jsonp.addParameter(target);
		return newFixedLengthResponse(NanoHTTPD.Response.Status.OK,
			RespUtils.MIME_APPLICATION_JAVASCRIPT,
			jsonp.toString()
		);
	}

	public static NanoHTTPD.Response ok(Object target) {
		return newFixedLengthResponse(NanoHTTPD.Response.Status.OK,
			RespUtils.MIME_APPLICATION_JSON,
			JSON.toJSONString(target)
		);
	}

	public static NanoHTTPD.Response ok(NanoHTTPD.IHTTPSession session, Object target) {
		List<String> callbackList = session.getParameters().get("callback");
		String callback = callbackList != null && callbackList.size() > 0 ? callbackList.get(0) : null;
		return callback != null ? jsonp(callback, target) : ok(target);
	}

}
