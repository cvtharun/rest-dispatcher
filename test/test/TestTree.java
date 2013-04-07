package test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;
import restdisp.urltree.LookupTree;
import restdisp.urltree.Node;
import restdisp.urltree.UrlDescriptor;
import restdisp.urltree.UrlTreeBuilder;
import restdisp.validation.ConfigurationException;
import restdisp.validation.HandlerException;
import restdisp.validation.RoutingException;
import restdisp.worker.TreeExecutor;

public class TestTree extends TestCase {
	public void testTree() throws IOException, SecurityException, ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, ConfigurationException, RoutingException, HandlerException, InterruptedException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.conf");
		Node root = new UrlTreeBuilder(null).buildUrlTree(is);
		
		UrlDescriptor res = null;
		long strt = System.currentTimeMillis();
		long end = 0;
		
		int i = 0;
		while (true) {
			res = LookupTree.getPath(root, "post", "/svc/act/1/2/3/a/b/c/d/e/4/5/6");
			new TreeExecutor().exec(res, null, null, null);
			
			end = System.currentTimeMillis();
			if (end - strt > 1000) {
				break;
			}
			i++;
		}
		end = System.currentTimeMillis();
		System.out.format("%d/%d mes/ms\n", i, (end - strt));
	}
}
