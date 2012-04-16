package restdisp.worker;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import restdisp.io.IOUtil;

public abstract class AbstractWorker {
	public static final String DEF_ENCODING = "UTF-8";
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext servletContext;
	
	public String getPayload() {
		String encoding = request.getCharacterEncoding();
		encoding = encoding == null ? DEF_ENCODING : encoding;
		return getPayload(encoding);
	}
	
	public String getPayload(String encoding) {
		try {
			return IOUtil.toString(request.getInputStream(), encoding);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setPayload(String str) {
		setPayload(str, DEF_ENCODING);
	}
	
	public void setPayload(String str, String encoding) {
		try {
			getResponse().getOutputStream().write(str.getBytes(encoding));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}

	void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
