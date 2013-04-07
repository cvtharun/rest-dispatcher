package restdisp.worker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import restdisp.urltree.Leaf;
import restdisp.urltree.UrlDescriptor;
import restdisp.validation.RoutingException;
import restdisp.validation.HandlerException;

public class TreeExecutor {
	public void exec(UrlDescriptor urlDescriptior, HttpServletRequest req, HttpServletResponse rsp, ServletContext servletContext) throws RoutingException, HandlerException {
		List<String> urlVariables = urlDescriptior.getUrlVariables();
		Leaf leaf = urlDescriptior.getLeaf();
		
		AbstractWorker abstractWorker = leaf.getAbstractWorker();
		abstractWorker.setRequest(req);
		abstractWorker.setResponse(rsp);
		abstractWorker.setServletContext(servletContext);
		
		Object[] vars;
		try {
			vars = getVariables(urlVariables, leaf.getMeth(), leaf.getCasters());
		} catch (RoutingException e) {
			throw new RoutingException(String.format("Failed to call method: [%s:%s()]", leaf.getCls().getName(), leaf.getMeth().getName()), e);
		}
		try {
			leaf.getMeth().invoke(abstractWorker, vars);
		} catch (IllegalAccessException e) {
			throw new RoutingException(String.format("Failed to access method [%s:%s]", leaf.getCls().getName(), leaf.getMeth().getName()), e);
		} catch (InvocationTargetException e) {
			throw new HandlerException(String.format("Handler invocation exception [%s:%s]. Variables count [%s].", leaf.getCls().getName(), leaf.getMeth().getName(), vars.length), e);
		}
	}
	
	private Object[] getVariables(List<String> list, Method meth, ArgCaster[] casters) throws RoutingException {
		if (null == list) {
			return new Object[0];
		}
		
		Object[] res = new Object[list.size()];
		int cnt = 0;
		for (String item : list) {
			try {
				res[cnt] = casters[cnt].cast(item);
				cnt++;
			} catch (Exception e) {
				Class<?>[] tps = meth.getParameterTypes();
				throw new RoutingException(String.format("Failed to cast variable for method call: ['%1$s' => %2$s]", item, tps[cnt]), e);
			}
		}
		return res;
	}
}
