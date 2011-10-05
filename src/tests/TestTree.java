package tests;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import restdisp.urltree.LookupTree;
import restdisp.urltree.Node;
import restdisp.urltree.UrlTreeBuilder;
import restdisp.urltree.UrlDescriptor;
import restdisp.validation.ConfigurationException;
import restdisp.validation.HandlerException;
import restdisp.validation.RoutingException;
import restdisp.worker.TreeExecutor;

public class TestTree {
	public static void main(String... args) throws IOException, SecurityException, ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, ConfigurationException, RoutingException, HandlerException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("router.conf");
		Node root = UrlTreeBuilder.buildUrlTree(is);
		
		UrlDescriptor res = null;
		long strt = System.currentTimeMillis();
		long end = 0;
		
		// w127995mps
		int i = 0;
		for (; i < 50000; i++) {
			res = LookupTree.getPath(root, "GET", "/web/add/1/456");
			TreeExecutor.exec(res, null, null);
		
			res = LookupTree.getPath(root, "post", "/web/add/1/456");
			TreeExecutor.exec(res, null, null);
			
			res = LookupTree.getPath(root, "post", "/web");
			TreeExecutor.exec(res, null, null);
			
			res = LookupTree.getPath(root, "POST", "/web/");
			TreeExecutor.exec(res, null, null);
		
			res = LookupTree.getPath(root, "get", "/123/456");
			TreeExecutor.exec(res, null, null);
			
			end = System.currentTimeMillis();
			if (end - strt > 1000) {
				break;
			}
		}
		end = System.currentTimeMillis();
		i *= 5;
		System.out.format("%d %d", i, (end - strt));
	}
	
	public static void scan(Node root) {
		System.out.format("%1$-8s %2$-8s \n", root.getName(), root.isVar());
		List<Node> lst = root.getChildren();

		if (lst != null) {
			for (Node node : lst) {
				scan(node);
			}
		}
	}
}
