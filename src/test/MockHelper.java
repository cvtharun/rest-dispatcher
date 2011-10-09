package test;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class MockHelper {
	private ByteArrayOutputStream baos;
	private HttpServletResponse mockRsp;
	
	public MockHelper(ByteArrayOutputStream baos) {
		this.baos = baos;
	}
	
	public HttpServletResponse getMockRsp() {
		return mockRsp;
	}
	
	public String getResult() throws IOException {
		mockRsp.getWriter().close();
		return new String(baos.toByteArray());
	}
	
	public void setMockRsp(HttpServletResponse mockRsp) {
		this.mockRsp = mockRsp;
	}
	
	public static MockHelper buildMock() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MockHelper mock = new MockHelper(baos);
		
		PrintWriter pw = new PrintWriter(baos);
		HttpServletResponse mockRsp = null;
		mockRsp = createStrictMock(HttpServletResponse.class);
		expect(mockRsp.getWriter()).andReturn(pw);
		expect(mockRsp.getWriter()).andReturn(pw);
		replay(mockRsp);
		
		mock.setMockRsp(mockRsp);
		
		return mock;
	}
}