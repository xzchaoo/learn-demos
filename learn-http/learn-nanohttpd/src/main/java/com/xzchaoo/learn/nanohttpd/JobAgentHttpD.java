package com.xzchaoo.learn.nanohttpd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Administrator on 2017/4/24.
 */
@Component
public class JobAgentHttpD extends NanoHTTPD {
	private static final Logger LOG = LoggerFactory.getLogger(JobAgentHttpD.class);

	@Autowired
	private JobAgentController controller;

	public JobAgentHttpD(JobAgentProperties jobAgentProperties) throws IOException {
		super(jobAgentProperties.getHostname(), jobAgentProperties.getPort());
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
	}

	@Override
	public Response serve(IHTTPSession session) {
		String uri = session.getUri();
		Method method = session.getMethod();
		if (RespUtils.CORS && method == Method.OPTIONS) {
			return RespUtils.CORS_RESPONSE;
		}
		Response response;
		if (uri.equals("/performance") && method == Method.GET) {
			response = controller.getPerformance(session);
		} else if (uri.equals("/executor") && method == Method.POST) {
			response = controller.executor(session);
		} else {
			response = RespUtils.BAD_REQUEST;
		}
		if (!response.isCloseConnection()) {
			response.closeConnection(true);
		}
		//response.addHeader("Connection", "close");
		return response;
	}
}
