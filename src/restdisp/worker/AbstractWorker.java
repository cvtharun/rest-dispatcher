package restdisp.worker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractWorker {
	private HttpServletRequest request;
	private HttpServletResponse response;
	
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
