package restdisp.validation;

import java.util.HashSet;
import java.util.Set;

public class ConfigParser {
	private static final Set<Class<?>> supportedArgs = new HashSet<Class<?>>();
	static {
		supportedArgs.add(boolean.class); supportedArgs.add(Boolean.class);
		supportedArgs.add(byte.class); supportedArgs.add(Byte.class);
		supportedArgs.add(char.class); supportedArgs.add(Character.class);
		supportedArgs.add(short.class); supportedArgs.add(Short.class);
		supportedArgs.add(int.class); supportedArgs.add(Integer.class);
		supportedArgs.add(long.class); supportedArgs.add(Long.class);
		supportedArgs.add(float.class); supportedArgs.add(Float.class);
		supportedArgs.add(double.class); supportedArgs.add(Double.class);
		supportedArgs.add(String.class);
	}

	public static String[] parse(String conf) {
		conf = conf.replaceAll("\r", "");
		conf = conf.replaceAll("[\\s&&[^\\n]]+", " "); // trim spaces inside entry
		conf = conf.replaceAll("(?<=\n)[\\s]+", ""); // trim spaces before entry
		String[] strEntries = conf.split("\n");
		return strEntries;
	}
	
	public static void validateUrl(String url) throws ConfigurationException {
		try {
			String[] elems = url.split("/");
			assertEquals(elems[0], "");
			for (int cnt = 1; cnt < elems.length; cnt++) {
				if (!elems[cnt].matches("^\\{.+\\}$") && !elems[cnt].matches("^(?!\\{).+(?<!\\})$")) {
					throw new ConfigurationException(String.format("Wrong value [%s]", elems[cnt]));
				}
			}
		} catch (ConfigurationException e) {
			throw new ConfigurationException(String.format("Wrong configuration entry [%s]", url), e);
		}
	}
	
	public static void validateMethod(String meth) throws ConfigurationException {
		if (!meth.equalsIgnoreCase("get") && !meth.equalsIgnoreCase("post") && !meth.equalsIgnoreCase("put") && !meth.equalsIgnoreCase("delete")){
			throw new ConfigurationException(String.format("Wrong method [%s]", meth));
		}
	}
	
	private static void assertEquals(String str1, String str2) throws ConfigurationException {
		if (!str1.equals(str2)) {
			throw new ConfigurationException(String.format("Got [%s] while expected [%s]", str1, str2));
		}
	}

	public static void validateMethArgs(Class<?>[] parameterTypes) throws ConfigurationException {
		for (Class<?> clazz : parameterTypes) {
			if (!supportedArgs.contains(clazz)) {
				throw new ConfigurationException(String.format("Unsupported argument type [%s]", clazz.getName()));
			}
		}
	}
}
