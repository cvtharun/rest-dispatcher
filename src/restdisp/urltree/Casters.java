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
				res[cnt] = getBoolean();
			} else if (cls == byte.class || cls == Byte.class) {
				res[cnt] = getByte();
			} else if (cls == short.class || cls == Short.class) {
				res[cnt] = getShort();
			} else if (cls == char.class || cls == Character.class) {
				res[cnt] = getCharacter();
			} else if (cls == int.class || cls == Integer.class) {
				res[cnt] = getInteger();
			} else if (cls == long.class || cls == Long.class) {
				res[cnt] = getLong();
			} else if (cls == float.class || cls == Float.class) {
				res[cnt] = getFloat();
			} else if (cls == double.class || cls == Double.class) {
				res[cnt] = getDouble();
			} else if (cls == String.class) {
				res[cnt] = getString();
			} else {
				throw new RuntimeException(String.format("Unsupported class detected [%s]", cls.getName()));
			}
			cnt++;
		}
		return res;
	}
	
	private static ArgCaster getBoolean() {
		return new ArgCaster() {
			public Object cast(String str) {
				return Boolean.parseBoolean(str);
			}
		};
	}
	
	private static ArgCaster getByte() {
		return new ArgCaster() {
			public Object cast(String str) {
				return Byte.parseByte(str);
			}
		};
	}
	
	private static ArgCaster getShort() {
		return new ArgCaster() {
			public Object cast(String str) {
				return Short.parseShort(str);
			}
		};
	}
	
	private static ArgCaster getCharacter() {
		return new ArgCaster() {
			public Object cast(String str) throws RoutingException {
				if (str.length() == 1) {
					return str.charAt(0);
				} else {
					throw new RoutingException(String.format("Failed to cast String to Character [%s]", str));
				}
			}
		};
	}
	
	private static ArgCaster getInteger() {
		return new ArgCaster() {
			public Object cast(String str) {
				return Integer.parseInt(str);
			}
		};
	}
	
	private static ArgCaster getLong() {
		return new ArgCaster() {
			public Object cast(String str) {
				return Long.parseLong(str);
			}
		};
	}
	
	private static ArgCaster getFloat() {
		return new ArgCaster() {
			public Object cast(String str) {
				return Float.parseFloat(str);
			}
		};
	}
	
	private static ArgCaster getDouble() {
		return new ArgCaster() {
			public Object cast(String str) {
				return Double.parseDouble(str);
			}
		};
	}
	
	private static ArgCaster getString() {
		return new ArgCaster() {
			public Object cast(String str) {
				return str;
			}
		};
	}
}
