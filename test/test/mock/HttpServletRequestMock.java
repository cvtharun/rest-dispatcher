package test.mock;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestMock {
	public static HttpServletRequest buildHttpServletRequest(String method, String requestURI) {
		HttpServletRequest mock = createStrictMock(HttpServletRequest.class);
		expect(mock.getMethod()).andReturn(method);
		expect(mock.getRequestURI()).andReturn(requestURI);
		replay(mock);
		
		return mock;
	}
}
