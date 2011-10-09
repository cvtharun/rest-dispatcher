package test;

import java.io.IOException;
import java.io.InputStream;

import restdisp.io.IOUtil;
import restdisp.urltree.LookupTree;
import restdisp.urltree.Node;
import restdisp.urltree.UrlDescriptor;
import restdisp.urltree.UrlTreeBuilder;
import restdisp.validation.HandlerException;
import restdisp.validation.RoutingException;
import restdisp.validation.ConfigurationException;
import restdisp.worker.TreeExecutor;
import test.MockHelper;
import static org.junit.Assert.*; 

import org.junit.Test;

public class ExceptionTest {
	private InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.conf");
	private Node root = UrlTreeBuilder.buildUrlTree(is);
	public ExceptionTest() throws Exception {}
	
	@Test(expected=HandlerException.class)
	public void HandlerException() throws HandlerException, IOException, RoutingException {
		MockHelper mock = MockHelper.buildMock();
		UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/exc");
		TreeExecutor.exec(res, null, mock.getMockRsp());
	}
	
	@Test(expected=RoutingException.class)
	@SuppressWarnings("unused")
	public void testRoutingException() throws HandlerException, IOException, RoutingException {
		MockHelper mock = MockHelper.buildMock();
		UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/exc/tst");
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testConfigurationExceptionBranch() throws ConfigurationException, IOException {
		try {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.branch.conf");
		Node root = UrlTreeBuilder.buildUrlTree(is);
		} catch (ConfigurationException e) {
			String excStr = IOUtil.getStackTrace(e);
			Throwable inner = e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Method not found ["));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testConfigurationExceptionMethod() throws ConfigurationException, IOException {
		try {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.method.conf");
		Node root = UrlTreeBuilder.buildUrlTree(is);
		} catch (ConfigurationException e) {
			String excStr = IOUtil.getStackTrace(e);
			Throwable inner = e.getCause();
			System.out.println(excStr);
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Method not found ["));
		}
	}
}
