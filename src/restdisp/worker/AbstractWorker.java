package restdisp.worker;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import restdisp.io.IOUtil;

public abstract class AbstractWorker {
	public static final String DEF_ENCODING = "UTF-8";
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public String getPayload() throws IOException {
		String encoding = request.getCharacterEncoding();
		encoding = encoding == null ? DEF_ENCODING : encoding;
		return getPayload(encoding);
	}
	
	public String getPayload(String encoding) throws IOException {
		return IOUtil.toString(request.getInputStream(), encoding);
	}
	
	public void setPayload(String str) throws UnsupportedEncodingException, IOException {
		setPayload(str, DEF_ENCODING);
	}
	
	public void setPayload(String str, String encoding) throws UnsupportedEncodingException, IOException {
		getResponse().getOutputStream().write(str.getBytes(encoding));
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}

	void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}
