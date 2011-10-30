package restdisp.validation;

public class ConfigParser {
	public static String[] parse(String conf) {
		conf = conf.replaceAll("\r", "");
		conf = conf.replaceAll("[\\s&&[^\\n]]+", " "); // skip extra spaces
		conf = conf.replaceAll("^[\\s]+|[\\s]+$", ""); // trim spaces
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
}
