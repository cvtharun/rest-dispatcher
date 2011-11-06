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
		
		Object[] vars;
		try {
			vars = getVariables(urlVariables, leaf.getMeth());
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
	
	@SuppressWarnings({"rawtypes"})
	private static Object[] getVariables(List<String> list, Method meth) throws RoutingException {
		if (null == list) {
			return new Object[0];
		}
		
		int cnt = 0;
		Class[] tps = meth.getParameterTypes();
		List<Object> lst = new ArrayList<Object>();
		for (String item : list) {
			String val = item;
			Class cls = tps[cnt];
			try {
				if (cls == boolean.class || cls == Boolean.class) {
					lst.add(Boolean.parseBoolean(val));
				} else if (cls == byte.class || cls == Byte.class) {
					lst.add(Byte.parseByte(val));
				} else if (cls == short.class || cls == Short.class) {
					lst.add(Short.parseShort(val));
				} else if (cls == char.class || cls == Character.class) {
					if (val.length() == 1) {
						lst.add(val.charAt(0));
					} else {
						throw new RoutingException(String.format("Failed to cast String to Character [%s]", val));
					}
				} else if (cls == int.class || cls == Integer.class) {
					lst.add(Integer.parseInt(val));
				} else if (cls == long.class || cls == Long.class) {
					lst.add(Long.parseLong(val));
				} else if (cls == float.class || cls == Float.class) {
					lst.add(Float.parseFloat(val));
				} else if (cls == double.class || cls == Double.class) {
					lst.add(Double.parseDouble(val));
				} else if (cls == String.class) {
					lst.add(val);
				} else {
					throw new RoutingException(String.format("Unsupported class detected [%s]", cls.getName()));
				}
				cnt++;
			} catch (Exception e) {
				throw new RoutingException(String.format("Failed to cast variable for method call: ['%1$s' => %2$s]", item, cls.getName()), e);
			}
		}
		return lst.toArray();
	}
}
