package test.mock;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;


public abstract class MockHelper {
	protected ByteArrayOutputStream baos;
	protected HttpServletResponse mock;
	
	public abstract String getResult() throws IOException;
	
	private MockHelper(ByteArrayOutputStream baos, HttpServletResponse mock) {
		this.baos = baos;
		this.mock = mock;
	}
	
	public HttpServletResponse getMock() {
		return mock;
	}
	
	public static MockHelper buildMock() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		HttpServletResponse mock = createStrictMock(HttpServletResponse.class);
		expect(mock.getWriter()).andReturn(pw);
		expect(mock.getWriter()).andReturn(pw);
		replay(mock);
		
		return new PrintMock(baos, mock);
	}
	
	public static MockHelper buildStreamMock() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		HttpServletResponse mock = createStrictMock(HttpServletResponse.class);
		expect(mock.getOutputStream()).andReturn(new ServletOutputStreamMock(baos));
		replay(mock);
		
		return new StreamMock(baos, mock);
	}
	
	private static class PrintMock extends MockHelper {
		private PrintMock(ByteArrayOutputStream baos, HttpServletResponse mock) {
			super(baos, mock);
		}
		public String getResult() throws IOException {
			mock.getWriter().close();
			baos.close();
			return new String(baos.toByteArray());
		}
	}
	
	private static class StreamMock extends MockHelper {
		private StreamMock(ByteArrayOutputStream baos, HttpServletResponse mock) {
			super(baos, mock);
		}
		public String getResult() throws IOException {
			baos.close();
			return new String(baos.toByteArray());
		}
	}
}

