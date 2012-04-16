package test.actors;

import restdisp.worker.AbstractWorker;

public class ActionError extends AbstractWorker {
	public void exec() {
		@SuppressWarnings("unused")
		int a = 5 / 0;
	}
}
