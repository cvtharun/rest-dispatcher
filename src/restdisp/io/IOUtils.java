package restdisp.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class IOUtils {
	private static final int BUF_SIZE = 1024;
	private static final String DEF_ENCODING = "UTF-8";

	public static byte[] readStream(final InputStream is) throws IOException {
		try {
			byte buf[] = new byte[BUF_SIZE];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			int read = 0;
			do {
				read = is.read(buf, 0, BUF_SIZE);
				if (read > 0) {
					bos.write(buf, 0, read);
				}
			} while (read > 0);

			byte[] result = bos.toByteArray();
			return result;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static String toString(final InputStream is) throws IOException {
		return toString(is, DEF_ENCODING);
	}
	
	public static String toString(final InputStream is, String encoding) throws IOException {
		byte[] buf = readStream(is);
		try {
			return new String(buf, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getStackTrace(Throwable exc) {
		String result = null;
		ByteArrayOutputStream ous = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(ous);
		exc.printStackTrace(ps);
		try {
			ps.close();
			ous.close();
			result = ous.toString(DEF_ENCODING);
		} catch (IOException e) {
			result = "Failed to execute getStackTrace: " + e.toString();
		}
		return result;
	}
}
