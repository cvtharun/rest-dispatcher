package restdisp.urltree;

import java.lang.reflect.Method;

import restdisp.worker.AbstractWorker;
import restdisp.worker.ArgCaster;

public class Leaf {
	private final Class<?> cls;
	private final AbstractWorker abstractWorker;
	private final Method meth;
	private final ArgCaster[] casters;
	
	public Leaf(Class<?> cls, AbstractWorker abstractWorker, Method meth, ArgCaster[] casters) {
		this.cls = cls;
		this.abstractWorker = abstractWorker;
		this.meth = meth;
		this.casters = casters;
	}
	
	public ArgCaster[] getCasters() {
		return casters;
	}

	public Class<?> getCls() {
		return cls;
	}

	public Method getMeth() {
		return meth;
	}

	public AbstractWorker getAbstractWorker() {
		return abstractWorker;
	}
}
