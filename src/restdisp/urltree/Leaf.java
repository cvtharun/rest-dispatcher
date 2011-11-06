package restdisp.urltree;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import restdisp.worker.ArgCaster;

public class Leaf {
	private Class<?> cls;
	private Constructor<?> constructor;
	private Method meth;
	private ArgCaster[] casters;
	
	public Leaf(Class<?> cls, Constructor<?> constructor, Method meth, ArgCaster[] casters) {
		this.cls = cls;
		this.constructor = constructor;
		this.meth = meth;
		this.casters = casters;
	}
	
	public ArgCaster[] getCasters() {
		return casters;
	}

	public Class<?> getCls() {
		return cls;
	}

	public Constructor<?> getConstructor() {
		return constructor;
	}

	public Method getMeth() {
		return meth;
	}
}
