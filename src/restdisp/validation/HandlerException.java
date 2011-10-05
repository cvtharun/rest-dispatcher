package restdisp.validation;

public class HandlerException extends Exception {
	private static final long serialVersionUID = 5640963737004755993L;

	public HandlerException(String str) {
		super(str);
	}
	
	public HandlerException(String str, Exception e) {
		super(str, e);
	}
}
