package test;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;
import restdisp.urltree.LookupTree;
import restdisp.urltree.Node;
import restdisp.urltree.UrlDescriptor;
import restdisp.urltree.UrlTreeBuilder;
import restdisp.validation.ConfigurationException;
import restdisp.validation.HandlerException;
import restdisp.validation.RoutingException;
import restdisp.worker.TreeExecutor;
import test.mock.DispatcherServletMock;
import test.mock.HttpServletRequestMock;
import test.mock.HttpServletResponseMock;

public class BasicTest extends TestCase {
	public void testLookup() throws IOException, RoutingException, HandlerException, ConfigurationException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.conf");
		Node root = new UrlTreeBuilder().buildUrlTree(is);
		
		HttpServletRequest httpServletRequestMock = createMock(HttpServletRequest.class);
		expect(httpServletRequestMock.getContentLength()).andReturn(1);
		replay(httpServletRequestMock);
		
		HttpServletResponseMock mock = new HttpServletResponseMock();
		UrlDescriptor res = LookupTree.getPath(root, "post", "/svc/act/Tarokun/1/2");
		TreeExecutor.exec(res, httpServletRequestMock, mock, null);
		assertEquals("Tarokun12", mock.getResult());
		
		verify(httpServletRequestMock);
		
		mock = new HttpServletResponseMock();
		res = LookupTree.getPath(root, "get", "/svc/act/123");
		TreeExecutor.exec(res, null, mock, null);
		assertEquals("123", mock.getResult());
		
		mock = new HttpServletResponseMock();
		res = LookupTree.getPath(root, "delete", "/svc/act/567");
		TreeExecutor.exec(res, null, mock, null);
		assertEquals("567", mock.getResult());
		
		mock = new HttpServletResponseMock();
		res = LookupTree.getPath(root, "get", "/svc/act");
		TreeExecutor.exec(res, null, mock, null);
		assertEquals("dummy", mock.getResult());
		
		mock = new HttpServletResponseMock();
		res = LookupTree.getPath(root, "get", "/svc/act/1.1/1.2/1.3/1.4");
		TreeExecutor.exec(res, null, mock, null);
		assertEquals("fp5", mock.getResult());
		
		mock = new HttpServletResponseMock();
		res = LookupTree.getPath(root, "get", "/svc/act/true/true/1/1/256/256/c/s");
		TreeExecutor.exec(res, null, mock, null);
		assertEquals("sttruetrue11256256cs", mock.getResult());
		
		mock = new HttpServletResponseMock();
		res = LookupTree.getPath(root, "get", "/svc/act/lt/1/2/3/4");
		TreeExecutor.exec(res, null, mock, null);
		assertEquals("lt1234", mock.getResult());
	}
	
	public void testRootPath() throws IOException, RoutingException, HandlerException, ConfigurationException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.rootpath.conf");
		Node root = new UrlTreeBuilder().buildUrlTree(is);
		
		HttpServletResponseMock mock = null;
		UrlDescriptor res = null;
		
		mock = new HttpServletResponseMock();
		res = LookupTree.getPath(root, "get", "/");
		TreeExecutor.exec(res, null, mock, null);
		assertEquals("dummy", mock.getResult());
		
		mock = new HttpServletResponseMock();
		res = LookupTree.getPath(root, "get", "/123");
		TreeExecutor.exec(res, null, mock, null);
		assertEquals("123", mock.getResult());
	}
	
	public void testDispatcherContextPath() throws IOException, ServletException {
		HttpServletResponseMock resp = new HttpServletResponseMock();
		DispatcherServletMock disp = new DispatcherServletMock("/web", "test/conf/router.disp.conf");
		HttpServletRequest req = HttpServletRequestMock.buildHttpServletRequest("get","/web/svc/act/123");
		
		disp.init();
		disp.service(req, resp);
		
		assertEquals("123", resp.getResult());
	}
	
	public void testDispatcherContextPathEmpty() throws IOException, ServletException {
		HttpServletResponseMock resp = new HttpServletResponseMock();
		DispatcherServletMock disp = new DispatcherServletMock("", "test/conf/router.disp.conf");
		HttpServletRequest req = HttpServletRequestMock.buildHttpServletRequest("get","/svc/act/123");
		
		disp.init();
		disp.service(req, resp);
		
		assertEquals("123", resp.getResult());
	}
	
	public void testDispatcherContext() throws IOException, ServletException {
		HttpServletResponseMock resp = new HttpServletResponseMock();
		DispatcherServletMock disp = new DispatcherServletMock("", "test/conf/router.disp.conf");
		
		ServletContext servletContext = createStrictMock(ServletContext.class);
		expect(servletContext.getContextPath()).andReturn("/web");
		expect(servletContext.getContextPath()).andReturn("/web");
		replay(servletContext);
		disp.setServletContext(servletContext);
		
		HttpServletRequest req = HttpServletRequestMock.buildHttpServletRequest("get","/svc/name");
		
		disp.init();
		disp.service(req, resp);
		
		verify(servletContext);
		
		assertEquals("Taro", resp.getResult());
	}
}

