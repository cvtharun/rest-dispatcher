package test;

import java.io.IOException;
import java.io.InputStream;

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
	
	@Test
	public void testHandlerException() throws ConfigurationException, IOException, RoutingException, restdisp.validation.HandlerException {
		try {
			MockHelper mock = MockHelper.buildMock();
			UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/exc");
			TreeExecutor.exec(res, null, mock.getMock());
			assertTrue(false);
		} catch (HandlerException e) {
			assertTrue(e.getMessage().contains("Handler invocation exception [test.actors.Action:getException]. Variables count [0]."));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testRoutingException() throws ConfigurationException, IOException, RoutingException, restdisp.validation.HandlerException {
		try {
			MockHelper mock = MockHelper.buildMock();
			UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/exc/tst");
			assertTrue(false);
		} catch (RoutingException e) {
			assertTrue(e.getMessage().contains("Path not defined [/get/svc/exc/tst]"));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testConfigurationExceptionBranch() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.branch.conf");
			Node root = UrlTreeBuilder.buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Method not found [class test.actors.Action:getExceptionCase]. Variables count [0]."));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testConfigurationExceptionMethod() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.method.conf");
			Node root = UrlTreeBuilder.buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Method not found [class test.actors.Action:getException]. Variables count [1]."));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testConfigurationExceptionGenMethod() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.genericmethod.conf");
			Node root = UrlTreeBuilder.buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Failed to build leaf. Default constructor not found [test.actors.ActionMethodErr]."));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testConfigurationGenClassException() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.genericclass.conf");
			Node root = UrlTreeBuilder.buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Failed to build leaf. Class not found [test.actors.ActionErr]."));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testMethodValiationException() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.methodvalidation.conf");
			Node root = UrlTreeBuilder.buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Wrong method [gets]"));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testUrlValValiationException() throws ConfigurationException, IOException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.urlvalvalidation.conf");
			Node root = UrlTreeBuilder.buildUrlTree(is);
			assertTrue(false);
		} catch (ConfigurationException e) {
			Throwable inner = e.getCause();
			Throwable inner2 = inner.getCause();
			assertTrue(e.getMessage().contains("Failed to add branch"));
			assertTrue(inner.getMessage().contains("Wrong configuration entry [/svc/exc/{id]"));
			assertTrue(inner2.getMessage().contains("Wrong value [{id]"));
		}
	}
	
	@Test
	public void testAbstractWorkerException() throws ConfigurationException, IOException, RoutingException, HandlerException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.abstractworkerexc.conf");
			Node root = UrlTreeBuilder.buildUrlTree(is);
			UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/exc/1");
			TreeExecutor.exec(res, null, null);
			assertTrue(false);
		} catch (RoutingException e) {
			assertTrue(e.getMessage().contains("Failed to instantiate worker [test.actors.UsrAbstractWorker]"));
		}
	}
	
	@Test
	public void testAbstractConstructorException() throws ConfigurationException, IOException, RoutingException, HandlerException {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.exception.consworkerexc.conf");
			Node root = UrlTreeBuilder.buildUrlTree(is);
			UrlDescriptor res = LookupTree.getPath(root, "get", "/svc/exc/1");
			TreeExecutor.exec(res, null, null);
			assertTrue(false);
		} catch (HandlerException e) {
			assertTrue(e.getMessage().contains("Constructor invocation exception [test.actors.ConstructorException]"));
		}
	}
}
