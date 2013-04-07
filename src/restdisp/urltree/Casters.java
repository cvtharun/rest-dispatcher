package restdisp.urltree;

import java.lang.reflect.Method;
import restdisp.validation.RoutingException;
import restdisp.worker.ArgCaster;

public class Casters {
	public static ArgCaster[] getCasters(Method meth) {
		Class<?>[] tps = meth.getParameterTypes();
		ArgCaster[] res = new ArgCaster[tps.length];
		int cnt = 0;
		for (Class<?> cls : tps) {
			if (cls == boolean.class || cls == Boolean.class) {
				res[cnt] = booleanCaster;
			} else if (cls == byte.class || cls == Byte.class) {
				res[cnt] = byteCaster;
			} else if (cls == short.class || cls == Short.class) {
				res[cnt] = shortCaster;
			} else if (cls == char.class || cls == Character.class) {
				res[cnt] = characterCaster;
			} else if (cls == int.class || cls == Integer.class) {
				res[cnt] = integerCaster;
			} else if (cls == long.class || cls == Long.class) {
				res[cnt] = longCaster;
			} else if (cls == float.class || cls == Float.class) {
				res[cnt] = floatCaster;
			} else if (cls == double.class || cls == Double.class) {
				res[cnt] = doubleCaster;
			} else if (cls == String.class) {
				res[cnt] = stringCaster;
			} else {
				throw new RuntimeException(String.format("Unsupported class detected [%s]", cls.getName()));
			}
			cnt++;
		}
		return res;
	}
	
	private static final ArgCaster booleanCaster = new ArgCaster() {
		public Object cast(String str) {
			return Boolean.parseBoolean(str);
		}
	};
		
	private static final ArgCaster byteCaster = new ArgCaster() {
		public Object cast(String str) {
			return Byte.parseByte(str);
		}
	};

	private static final ArgCaster shortCaster = new ArgCaster() {
		public Object cast(String str) {
			return Short.parseShort(str);
		}
	};

	private static final ArgCaster characterCaster = new ArgCaster() {
		public Object cast(String str) throws RoutingException {
			if (str.length() == 1) {
				return str.charAt(0);
			} else {
				throw new RoutingException(String.format("Failed to cast String to Character [%s]", str));
			}
		}
	};
	
	private static final ArgCaster integerCaster = new ArgCaster() {
		public Object cast(String str) {
			return Integer.parseInt(str);
		}
	};
	
	private static final ArgCaster longCaster = new ArgCaster() {
		public Object cast(String str) {
			return Long.parseLong(str);
		}
	};
	
	private static final ArgCaster floatCaster = new ArgCaster() {
		public Object cast(String str) {
			return Float.parseFloat(str);
		}
	};
	
	private static final ArgCaster doubleCaster = new ArgCaster() {
		public Object cast(String str) {
			return Double.parseDouble(str);
		}
	};
	
	private static final ArgCaster stringCaster = new ArgCaster() {
		public Object cast(String str) {
			return str;
		}
	};
}
