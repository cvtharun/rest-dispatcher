package restdisp.worker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractWorker {
	private HttpServletRequest req;
	private HttpServletResponse rsp;
	
	public AbstractWorker(HttpServletRequest req, HttpServletResponse rsp) {
		this.req = req;
		this.rsp = rsp;
	}
	
	public HttpServletRequest getRequest() {
		return req;
	}
	
	public HttpServletResponse getResponse() {
		return rsp;
	}
}
