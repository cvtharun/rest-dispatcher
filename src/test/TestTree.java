package test;

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
	public static void main(String... args) throws IOException, SecurityException, ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, ConfigurationException, RoutingException, HandlerException, InterruptedException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/conf/router.conf");
		Node root = UrlTreeBuilder.buildUrlTree(is);
		
		UrlDescriptor res = null;
		long strt = System.currentTimeMillis();
		long end = 0;
		
		int i = 0;
		for (; i < 500000000; i++) {
			res = LookupTree.getPath(root, "post", "/svc/act/1/2/3/a/b/c/d/e/4/5/6");
			TreeExecutor.exec(res, null, null);
			
			end = System.currentTimeMillis();
			if (end - strt > 1000) {
				break;
			}
		}
		end = System.currentTimeMillis();
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
