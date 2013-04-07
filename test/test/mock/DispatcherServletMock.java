package test.mock;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import restdisp.DispatcherServlet;

public class DispatcherServletMock extends DispatcherServlet {
	private static final long serialVersionUID = 1L;
	private String contextPath;
	private String initParameter;
	private ServletContext servletContext;
	
	private int callCnt = 0;
	
	public DispatcherServletMock(String servletContext, String initParameter) {
		this.contextPath = servletContext;
		this.initParameter = initParameter;
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		if (callCnt == 0) {
			callCnt++;
			return null;
		}
		
		if (servletContext != null) 
			return servletContext;
		
		ServletContext mock = createStrictMock(ServletContext.class);
		expect(mock.getContextPath()).andReturn(contextPath);
		replay(mock);
		
		return mock;
	}
	
	public ServletConfig getServletConfig() {
		ServletConfig mock = createStrictMock(ServletConfig.class);
		expect(mock.getInitParameter("router.conf")).andReturn(initParameter);
		replay(mock);
		
		return mock;
	}
}
