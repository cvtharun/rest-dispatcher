package tests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import restdisp.worker.AbstractWorker;

public class Action extends AbstractWorker  {
	
	public Action(HttpServletRequest req, HttpServletResponse rsp) {
		super(req, rsp);
//		int a = 5/0;
	}
	
	public void addUser(String name) {
//		System.out.println("addUser: " + name);
	}
	
	public void addUser(String name, long id) {
//		System.out.println("addUser: " + name + " " + id);
//		int a = 5/0;
	}
	
	public void addUser2(String name, int id) {
//		System.out.println("addUser2: " + name + " " + id);
	}
	
	public void removeUser(String name) {
//		System.out.println("removeUser: " + name);
	}
	
	public void getUsers() {
//		System.out.println("getUsers");
	}

}
