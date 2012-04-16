package test.actors;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import restdisp.worker.AbstractWorker;

public class Action extends AbstractWorker  {
	public void highLoad(String name, long id, int cardId, String val, Long lon, Integer intgr) throws IOException {
//		System.out.println("addUser: " + name);
//		getResponse().getWriter().write(name + id + cardId);
	}
	
	public void addUser(String name, long id, int cardId) throws IOException {
		System.out.println("addUser: " + name);
		setPayload(name + id + cardId);
	}
	
	public void getUser(int id) throws IOException {
		System.out.println("getUser: " + id);
		setPayload("" + id, "UTF-8");
	}
	
	public void getFloatingPoint(float a, double b, Float c, Double d) throws IOException {
		Long res = Math.round(a + b + c + d);
		System.out.println("getFloatingPoint: " + res);
		getResponse().getWriter().write("fp" + res.toString());
	}
	
	public void getShortTypes(boolean a, Boolean b, byte c, Byte d, short e, Short f, char g, Character h) throws IOException {
		String str = "st" + a + b + c + d + e + f + g + h;
		System.out.println("getShortTypes: " + str);
		getResponse().getWriter().write(str);
	}
	
	public void getLongTypes(int a, Integer b, long c, Long d) throws IOException {
		String str = "lt" + a + b + c + d;
		System.out.println("getLongTypes: " + str);
		getResponse().getWriter().write(str);
	}
	
	public void removeUser(int id) throws IOException {
		System.out.println("removeUser: " + id);
		getResponse().getWriter().write("" + id);
	}
	
	public void getUsers() throws IOException {
		System.out.println("getUsers");
		getResponse().getWriter().write("dummy");
	}
	
	public void getException() throws IOException {
		@SuppressWarnings("unused")
		int a = 5 / 0;
	}
	
	public void getName() throws UnsupportedEncodingException, IOException {
		getServletContext().getContextPath();
		setPayload("Taro");
	}
}
