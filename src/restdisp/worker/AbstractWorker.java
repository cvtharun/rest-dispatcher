package restdisp.worker;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import restdisp.io.IOUtils;

public abstract class AbstractWorker {
	public static final String DEF_ENCODING = "UTF-8";
	private final ThreadLocal<HttpServletRequest> request = new  ThreadLocal<HttpServletRequest>();
	private final ThreadLocal<HttpServletResponse> response = new  ThreadLocal<HttpServletResponse>();
	private final ThreadLocal<ServletContext> servletContext = new  ThreadLocal<ServletContext>();
	
	public String getPayload() {
		String encoding = request.get().getCharacterEncoding();
		encoding = encoding == null ? DEF_ENCODING : encoding;
		return getPayload(encoding);
	}
	
	public String getPayload(String encoding) {
		try {
			return IOUtils.toString(request.get().getInputStream(), encoding);
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
		return request.get();
	}
	
	public HttpServletResponse getResponse() {
		return response.get();
	}
	
	public ServletContext getServletContext() {
		return servletContext.get();
	}

	void setRequest(HttpServletRequest request) {
		this.request.set(request);
	}

	void setResponse(HttpServletResponse response) {
		this.response.set(response);
	}
	
	void setServletContext(ServletContext servletContext) {
		this.servletContext.set(servletContext);
	}
}
