package restdisp.validation;

public class ConfigurationException extends Exception {
	private static final long serialVersionUID = -4617711854858238545L;
	
	public ConfigurationException(String str) {
		super(str);
	}
	
	public ConfigurationException(String str, Exception e) {
		super(str, e);
	}
}
