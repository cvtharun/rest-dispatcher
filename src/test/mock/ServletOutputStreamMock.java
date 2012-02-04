package test.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServletOutputStreamMock extends  javax.servlet.ServletOutputStream {
	ByteArrayOutputStream baos;
	ServletOutputStreamMock(ByteArrayOutputStream baos) {
		this.baos = baos;
	}
	
	@Override
	public void write(int b) throws IOException {}

	public void write(byte[] b) throws IOException {
		baos.write(b);
	}
}
