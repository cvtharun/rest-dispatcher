package restdisp.urltree;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@SuppressWarnings("rawtypes") 
public class Leaf {
	private Class cls;
	private Constructor constructor;
	private Method meth;
	
	public Leaf(Class cls, Constructor constructor, Method meth) {
		this.cls = cls;
		this.constructor = constructor;
		this.meth = meth;
	}
	
	public Class getCls() {
		return cls;
	}

	public Constructor getConstructor() {
		return constructor;
	}

	public Method getMeth() {
		return meth;
	}
}
