package restdisp.worker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import restdisp.urltree.Leaf;
import restdisp.urltree.UrlDescriptor;
import restdisp.validation.RoutingException;
import restdisp.validation.HandlerException;

public class TreeExecutor {
	public static void exec(UrlDescriptor urlDescriptior, HttpServletRequest req, HttpServletResponse rsp) throws RoutingException, HandlerException {
		List<String> urlVariables = urlDescriptior.getUrlVariables();
		Leaf leaf = urlDescriptior.getLeaf();
		
		AbstractWorker abstractWorker;
		try {
			abstractWorker = (AbstractWorker) leaf.getConstructor().newInstance();
			abstractWorker.setRequest(req);
			abstractWorker.setResponse(rsp);
		} catch (InstantiationException e) {
			throw new RoutingException(String.format("Failed to instantiate worker [%s]", leaf.getCls().getName()), e);
		} catch (IllegalAccessException e) {
			throw new RoutingException(String.format("Illegal acces to worker [%s]", leaf.getCls().getName()), e);
		} catch (InvocationTargetException e) {
			throw new HandlerException(String.format("Constructor invocation exception [%s]", leaf.getCls().getName()), e);
		}
		
		Object[] vars = getVariables(urlVariables, leaf.getMeth(), leaf.getCls().toString());
		try {
			leaf.getMeth().invoke(abstractWorker, vars);
		} catch (IllegalAccessException e) {
			throw new RoutingException(String.format("Failed to access method [%s:%s]", leaf.getCls().getName(), leaf.getMeth().getName()), e);
		} catch (InvocationTargetException e) {
			throw new HandlerException(String.format("Handler invocation exception [%s:%s]. Variables count [%s].", leaf.getCls().getName(), leaf.getMeth().getName(), vars.length), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Object[] getVariables(List<String> list, Method meth, String className) throws RoutingException {
		if (null == list) {
			return new Object[0];
		}
		
		int cnt = 0;
		@SuppressWarnings("rawtypes")
		Class[] tps = meth.getParameterTypes();
		List<Object> lst = new ArrayList<Object>();
		for (String item : list) {
			String val = item;
			try {
				if (tps[cnt].isAssignableFrom(int.class)) {
					lst.add(Integer.parseInt(val));
				} else if (tps[cnt].isAssignableFrom(long.class)) {
					lst.add(Long.parseLong(val));
				} else if (tps[cnt].isAssignableFrom(java.lang.Integer.class)) {
					lst.add(Integer.valueOf(val));
				} else if (tps[cnt].isAssignableFrom(java.lang.Long.class)) {
					lst.add(Long.valueOf(val));
				} else if (tps[cnt].isAssignableFrom(java.lang.String.class)) {
					lst.add(val);
				}
				cnt++;
			} catch (NumberFormatException e) {
				throw new RoutingException(String.format("Failed to cast: %s => %s [%s:%s()]", item, tps[cnt].getName(), className, meth.getName()));
			}
		}
		return lst.toArray();
	}
}
