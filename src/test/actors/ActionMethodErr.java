package test.actors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import restdisp.worker.AbstractWorker;

public class ActionMethodErr extends AbstractWorker  {
	public ActionMethodErr(HttpServletRequest req, HttpServletResponse rsp, int a) {
		super(req, rsp);
	}
}
