package restdisp.validation;

public class ConfigParser {
	public static String[] parse(String conf) {
		conf = conf.replaceAll("\n", "");
		conf = conf.replaceAll("[\\s&&[^\\r]]+", " "); // skip extra spaces
		conf = conf.replaceAll("^[\\s]+|[\\s]+$", ""); // trim spaces
		String[] strEntries = conf.split("\r");
		return strEntries;
	}
}
