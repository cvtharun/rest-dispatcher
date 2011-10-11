package test;

import java.io.IOException;
import java.io.InputStream;

import restdisp.urltree.LookupTree;
import restdisp.urltree.Node;
import restdisp.urltree.UrlDescriptor;
import restdisp.urltree.UrlTreeBuilder;
import restdisp.validation.HandlerException;
import restdisp.validation.RoutingException;
import restdisp.worker.TreeExecutor;
import junit.framework.TestCase;

public class BasicTest extends TestCase {
	InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.conf");
	Node root = UrlTreeBuilder.buildUrlTree(is);
	public BasicTest() throws Exception {}
	
	public void testLookup() throws IOException, RoutingException, HandlerException {
		MockHelper mock = MockHelper.buildMock();
		UrlDescriptor res = LookupTree.getPath(root, "post", "/svc/act/Tarokun/1/2");
		TreeExecutor.exec(res, null, mock.getMock());
		assertEquals("Tarokun12", mock.getResult());
		
		mock = MockHelper.buildMock();
		res = LookupTree.getPath(root, "get", "/svc/act/123");
		TreeExecutor.exec(res, null, mock.getMock());
		assertEquals("123", mock.getResult());
		
		mock = MockHelper.buildMock();
		res = LookupTree.getPath(root, "delete", "/svc/act/567");
		TreeExecutor.exec(res, null, mock.getMock());
		assertEquals("567", mock.getResult());
		
		mock = MockHelper.buildMock();
		res = LookupTree.getPath(root, "get", "/svc/act");
		TreeExecutor.exec(res, null, mock.getMock());
		assertEquals("dummy", mock.getResult());
	}
}
