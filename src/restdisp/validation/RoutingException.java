package restdisp.validation;

public class RoutingException extends Exception {
	private static final long serialVersionUID = 2979684656917867776L;

	public RoutingException(String str) {
		super(str);
	}
	
	public RoutingException(String str, Exception e) {
		super(str, e);
	}
}
