package test.actors;

import restdisp.worker.AbstractWorker;

public class ConstructorException extends AbstractWorker {
	public ConstructorException() {
		@SuppressWarnings("unused")
		int a = 1/0;
	}
	
	public void exec(int a){}
}
